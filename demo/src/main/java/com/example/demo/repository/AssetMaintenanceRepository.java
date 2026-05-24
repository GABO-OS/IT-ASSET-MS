package com.example.demo.repository;

import com.example.demo.models.AssetMaintenance;
import com.example.demo.models.AssetMaintenance.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for AssetMaintenance entity operations.
 */
@Repository
public interface AssetMaintenanceRepository extends JpaRepository<AssetMaintenance, Long> {
    
    // Retrieve all maintenance logs for a specific asset
    List<AssetMaintenance> findByAssetId(Long assetId);
    
    // Retrieve all maintenance records filtered by status
    List<AssetMaintenance> findByStatus(MaintenanceStatus status);
}
