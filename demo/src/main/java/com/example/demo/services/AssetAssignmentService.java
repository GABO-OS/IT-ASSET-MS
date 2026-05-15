package com.example.demo.services;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.models.Asset;
import com.example.demo.models.Asset.AssetStatus;
import com.example.demo.models.AssetAssignment;
import com.example.demo.models.AssetAssignment.AssignmentStatus;
import com.example.demo.models.User;
import com.example.demo.repository.AssetAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Service para sa pag-assign at pag-return ng IT assets
// Ito yung pinaka-importanteng business logic ng system
@Service
@RequiredArgsConstructor
public class AssetAssignmentService {

    private final AssetAssignmentRepository assignmentRepository;
    private final AssetService assetService;   // Para ma-update ang status ng asset
    private final UserService userService;     // Para ma-validate ang user

    // ===================== ASSIGN ASSET =====================

    // Mag-assign ng asset sa isang empleyado
    public AssetAssignment assignAsset(AssignmentRequest request) {
        // Kunin ang asset at user mula sa database
        Asset asset = assetService.getAssetById(request.getAssetId());
        User user = userService.getUserById(request.getUserId());

        // I-check kung AVAILABLE pa ang asset - hindi pwedeng i-assign ang IN_USE na asset
        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new RuntimeException(
                "Hindi pwedeng i-assign ang asset na '" + asset.getName() +
                "' kasi ang status nito ay: " + asset.getStatus()
            );
        }

        // I-check din kung may existing na ACTIVE assignment ang asset
        boolean alreadyAssigned = assignmentRepository
                .existsByAssetIdAndStatus(asset.getId(), AssignmentStatus.ACTIVE);
        if (alreadyAssigned) {
            throw new RuntimeException("May naka-assign na sa asset na ito!");
        }

        // Gumawa ng bagong assignment record
        AssetAssignment assignment = new AssetAssignment();
        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setStatus(AssignmentStatus.ACTIVE);
        assignment.setAssignedBy(request.getAssignedBy());
        assignment.setNotes(request.getNotes());

        // Kung walang petsa na ibinigay, today na lang
        assignment.setAssignedDate(
            request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now()
        );

        // I-update ang status ng asset sa IN_USE
        assetService.updateAssetStatus(asset.getId(), AssetStatus.IN_USE);

        // I-save ang assignment
        return assignmentRepository.save(assignment);
    }

    // ===================== RETURN ASSET =====================

    // I-process ang pagbabalik ng asset
    public AssetAssignment returnAsset(Long assignmentId) {
        // Hanapin ang assignment record
        AssetAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException(
                    "Hindi mahanap ang assignment na may ID: " + assignmentId));

        // I-check kung ACTIVE pa ang assignment - hindi pwedeng i-return ang already returned
        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new RuntimeException("Hindi na ACTIVE ang assignment na ito - status: " + assignment.getStatus());
        }

        // I-update ang assignment - markahan as RETURNED at lagyan ng return date
        assignment.setStatus(AssignmentStatus.RETURNED);
        assignment.setReturnedDate(LocalDate.now()); // Ngayon ang return date

        // I-update ang asset status pabalik sa AVAILABLE
        assetService.updateAssetStatus(assignment.getAsset().getId(), AssetStatus.AVAILABLE);

        return assignmentRepository.save(assignment);
    }

    // ===================== MARK AS LOST =====================

    // I-mark ang asset na nawala
    public AssetAssignment markAsLost(Long assignmentId) {
        AssetAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException(
                    "Hindi mahanap ang assignment na may ID: " + assignmentId));

        // I-update ang assignment status sa LOST
        assignment.setStatus(AssignmentStatus.LOST);

        // I-update din ang asset status sa LOST
        assetService.updateAssetStatus(assignment.getAsset().getId(), AssetStatus.LOST);

        return assignmentRepository.save(assignment);
    }

    // ===================== READ =====================

    // Kunin lahat ng assignments (para sa overview ng admin)
    public List<AssetAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // Kunin lahat ng ACTIVE assignments (kung sino may hawak ng asset ngayon)
    public List<AssetAssignment> getActiveAssignments() {
        return assignmentRepository.findByStatus(AssignmentStatus.ACTIVE);
    }

    // Kunin lahat ng assignments ng isang specific na user
    public List<AssetAssignment> getAssignmentsByUser(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }

    // Kunin ang history ng isang specific na asset
    public List<AssetAssignment> getAssignmentsByAsset(Long assetId) {
        return assignmentRepository.findByAssetId(assetId);
    }

    // Hanapin ang isang assignment gamit ang ID
    public AssetAssignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang assignment na may ID: " + id));
    }
}
