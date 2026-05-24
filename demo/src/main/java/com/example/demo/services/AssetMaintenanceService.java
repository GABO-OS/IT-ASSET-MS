package com.example.demo.services;

import com.example.demo.dto.MaintenanceRequest;
import com.example.demo.models.Asset;
import com.example.demo.models.AssetMaintenance;
import com.example.demo.repository.AssetMaintenanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service to manage maintenance workflows, repair logs, and asset status updates.
 */
@Service
@SuppressWarnings("null")
public class AssetMaintenanceService {

    private final AssetMaintenanceRepository maintenanceRepository;
    private final AssetService assetService;

    // Manual Constructor Injection
    public AssetMaintenanceService(
            AssetMaintenanceRepository maintenanceRepository,
            AssetService assetService) {
        this.maintenanceRepository = maintenanceRepository;
        this.assetService = assetService;
    }

    /**
     * Registers a new asset repair log and changes the asset status to UNDER_REPAIR.
     */
    public AssetMaintenance registerMaintenance(MaintenanceRequest request) {
        Asset asset = assetService.getAssetById(request.getAssetId());

        if (asset.getStatus() != Asset.AssetStatus.AVAILABLE) {
            throw new RuntimeException(
                "Hindi pwedeng i-maintenance ang asset na '" + asset.getName() + 
                "' dahil ang kasalukuyang status nito ay: " + asset.getStatus()
            );
        }

        AssetMaintenance maintenance = new AssetMaintenance();
        maintenance.setAsset(asset);
        maintenance.setIssue(request.getIssue());
        maintenance.setStartDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now());
        maintenance.setStatus(AssetMaintenance.MaintenanceStatus.PENDING);
        maintenance.setCost(request.getCost());
        maintenance.setRemarks(request.getRemarks());

        // Update the asset status automatically to UNDER_REPAIR
        assetService.updateAssetStatus(asset.getId(), Asset.AssetStatus.UNDER_REPAIR);

        return maintenanceRepository.save(maintenance);
    }

    /**
     * Resolves a pending maintenance log and updates the asset status accordingly.
     */
    public AssetMaintenance resolveMaintenance(Long maintenanceId, AssetMaintenance.MaintenanceStatus resolution, String remarks, Double cost) {
        AssetMaintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang maintenance log na may ID: " + maintenanceId));

        if (maintenance.getStatus() != AssetMaintenance.MaintenanceStatus.PENDING) {
            throw new RuntimeException("Tapos na o tapos nang na-resolve ang maintenance na ito!");
        }

        if (resolution == AssetMaintenance.MaintenanceStatus.PENDING) {
            throw new IllegalArgumentException("Dapat COMPLETED o IRREPARABLE ang piliing resolution status.");
        }

        maintenance.setStatus(resolution);
        maintenance.setEndDate(LocalDate.now());
        maintenance.setRemarks(remarks);
        if (cost != null) {
            maintenance.setCost(cost);
        }

        // Auto-update corresponding asset status base sa repair result
        if (resolution == AssetMaintenance.MaintenanceStatus.COMPLETED) {
            assetService.updateAssetStatus(maintenance.getAsset().getId(), Asset.AssetStatus.AVAILABLE);
        } else if (resolution == AssetMaintenance.MaintenanceStatus.IRREPARABLE) {
            assetService.updateAssetStatus(maintenance.getAsset().getId(), Asset.AssetStatus.DISPOSED);
        }

        return maintenanceRepository.save(maintenance);
    }

    /**
     * Retrieve all maintenance logs in the database.
     */
    public List<AssetMaintenance> getAllMaintenanceLogs() {
        return maintenanceRepository.findAll();
    }

    /**
     * Retrieve a specific maintenance record by its unique ID.
     */
    public AssetMaintenance getMaintenanceLogById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang maintenance log na may ID: " + id));
    }
    
    /**
     * Retrieve all maintenance logs for a specific asset.
     */
    public List<AssetMaintenance> getLogsByAsset(Long assetId) {
        return maintenanceRepository.findByAssetId(assetId);
    }
}
