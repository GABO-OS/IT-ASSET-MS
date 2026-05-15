package com.example.demo.services;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.models.Asset;
import com.example.demo.models.AssetAssignment;
import com.example.demo.models.User;
import com.example.demo.repository.AssetAssignmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssetAssignmentService {

    private final AssetAssignmentRepository assignmentRepository;
    private final AssetService assetService;
    private final UserService userService;

    // Manual Constructor
    public AssetAssignmentService(
            AssetAssignmentRepository assignmentRepository,
            AssetService assetService,
            UserService userService) {
        this.assignmentRepository = assignmentRepository;
        this.assetService = assetService;
        this.userService = userService;
    }

    public AssetAssignment assignAsset(AssignmentRequest request) {
        Asset asset = assetService.getAssetById(request.getAssetId());
        User user = userService.getUserById(request.getUserId());

        if (asset.getStatus() != Asset.AssetStatus.AVAILABLE) {
            throw new RuntimeException(
                "Hindi pwedeng i-assign ang asset na '" + asset.getName() +
                "' kasi ang status nito ay: " + asset.getStatus()
            );
        }

        boolean alreadyAssigned = assignmentRepository
                .existsByAssetIdAndStatus(asset.getId(), AssetAssignment.AssignmentStatus.ACTIVE);
        if (alreadyAssigned) {
            throw new RuntimeException("May naka-assign na sa asset na ito!");
        }

        AssetAssignment assignment = new AssetAssignment();
        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setStatus(AssetAssignment.AssignmentStatus.ACTIVE);
        assignment.setAssignedBy(request.getAssignedBy());
        assignment.setNotes(request.getNotes());

        assignment.setAssignedDate(
            request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now()
        );

        assetService.updateAssetStatus(asset.getId(), Asset.AssetStatus.IN_USE);

        return assignmentRepository.save(assignment);
    }

    public AssetAssignment returnAsset(Long assignmentId) {
        AssetAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException(
                    "Hindi mahanap ang assignment na may ID: " + assignmentId));

        if (assignment.getStatus() != AssetAssignment.AssignmentStatus.ACTIVE) {
            throw new RuntimeException("Hindi na ACTIVE ang assignment na ito - status: " + assignment.getStatus());
        }

        assignment.setStatus(AssetAssignment.AssignmentStatus.RETURNED);
        assignment.setReturnedDate(LocalDate.now());

        assetService.updateAssetStatus(assignment.getAsset().getId(), Asset.AssetStatus.AVAILABLE);

        return assignmentRepository.save(assignment);
    }

    public AssetAssignment markAsLost(Long assignmentId) {
        AssetAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException(
                    "Hindi mahanap ang assignment na may ID: " + assignmentId));

        assignment.setStatus(AssetAssignment.AssignmentStatus.LOST);
        assetService.updateAssetStatus(assignment.getAsset().getId(), Asset.AssetStatus.LOST);

        return assignmentRepository.save(assignment);
    }

    public List<AssetAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<AssetAssignment> getActiveAssignments() {
        return assignmentRepository.findByStatus(AssetAssignment.AssignmentStatus.ACTIVE);
    }

    public List<AssetAssignment> getAssignmentsByUser(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }

    public List<AssetAssignment> getAssignmentsByAsset(Long assetId) {
        return assignmentRepository.findByAssetId(assetId);
    }

    public AssetAssignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang assignment na may ID: " + id));
    }
}
