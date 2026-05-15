package com.example.demo.controller;

import com.example.demo.dto.AssetRequest;
import com.example.demo.models.Asset;
import com.example.demo.services.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    private final AssetService assetService;

    // Manual Constructor
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody AssetRequest request) {
        Asset asset = assetService.createAsset(request);
        return ResponseEntity.ok(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Asset>> getAssetsByStatus(@PathVariable Asset.AssetStatus status) {
        return ResponseEntity.ok(assetService.getAssetsByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Asset>> getAssetsByType(@PathVariable Asset.AssetType type) {
        return ResponseEntity.ok(assetService.getAssetsByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchAssets(@RequestParam String name) {
        return ResponseEntity.ok(assetService.searchAssets(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody AssetRequest request) {
        return ResponseEntity.ok(assetService.updateAsset(id, request));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Asset> updateAssetStatus(@PathVariable Long id, @PathVariable Asset.AssetStatus status) {
        return ResponseEntity.ok(assetService.updateAssetStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Matagumpay na natanggal ang asset!");
    }
}
