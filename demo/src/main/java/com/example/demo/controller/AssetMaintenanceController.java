package com.example.demo.controller;

import com.example.demo.dto.MaintenanceRequest;
import com.example.demo.models.AssetMaintenance;
import com.example.demo.services.AssetMaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to expose REST APIs for registering, retrieving, and resolving repairs.
 */
@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class AssetMaintenanceController {

    private final AssetMaintenanceService maintenanceService;

    // Manual Constructor
    public AssetMaintenanceController(AssetMaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    /**
     * Endpoint to create a new maintenance record.
     * POST http://localhost:8080/api/maintenance
     */
    @PostMapping
    public ResponseEntity<AssetMaintenance> registerMaintenance(@RequestBody MaintenanceRequest request) {
        AssetMaintenance log = maintenanceService.registerMaintenance(request);
        return ResponseEntity.ok(log);
    }

    /**
     * Endpoint to fetch all maintenance records.
     * GET http://localhost:8080/api/maintenance
     */
    @GetMapping
    public ResponseEntity<List<AssetMaintenance>> getAllMaintenanceLogs() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceLogs());
    }

    /**
     * Endpoint to fetch a specific repair log by id.
     * GET http://localhost:8080/api/maintenance/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetMaintenance> getMaintenanceLogById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceLogById(id));
    }

    /**
     * Endpoint to fetch all maintenance logs for an asset.
     * GET http://localhost:8080/api/maintenance/asset/{assetId}
     */
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetMaintenance>> getLogsByAsset(@PathVariable Long assetId) {
        return ResponseEntity.ok(maintenanceService.getLogsByAsset(assetId));
    }

    /**
     * Endpoint to resolve a pending maintenance log.
     * PATCH http://localhost:8080/api/maintenance/{id}/resolve
     */
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<AssetMaintenance> resolveMaintenance(
            @PathVariable Long id,
            @RequestParam AssetMaintenance.MaintenanceStatus status,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) Double cost) {
        AssetMaintenance log = maintenanceService.resolveMaintenance(id, status, remarks, cost);
        return ResponseEntity.ok(log);
    }
}
