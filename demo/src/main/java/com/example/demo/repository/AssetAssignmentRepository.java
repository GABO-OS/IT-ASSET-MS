package com.example.demo.repository;

import com.example.demo.models.AssetAssignment;
import com.example.demo.models.AssetAssignment.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository para sa AssetAssignment - track kung sino may hawak ng asset
@Repository
public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Long> {

    // Kunin lahat ng assignments ng specific na user
    List<AssetAssignment> findByUserId(Long userId);

    // Kunin lahat ng assignments ng specific na asset
    List<AssetAssignment> findByAssetId(Long assetId);

    // Kunin lahat ng ACTIVE assignments (hindi pa naibalik ang asset)
    List<AssetAssignment> findByStatus(AssignmentStatus status);

    // Hanapin ang kasalukuyang active assignment ng isang asset
    // Ginagamit ito para malaman kung sino ang may hawak ng asset ngayon
    Optional<AssetAssignment> findByAssetIdAndStatus(Long assetId, AssignmentStatus status);

    // Kunin lahat ng active assignments ng isang user
    List<AssetAssignment> findByUserIdAndStatus(Long userId, AssignmentStatus status);

    // I-check kung may active assignment na ang asset (para hindi ma-double assign)
    boolean existsByAssetIdAndStatus(Long assetId, AssignmentStatus status);
}
