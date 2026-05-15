package com.example.demo.controller;

import com.example.demo.dto.AssetRequest;
import com.example.demo.models.Asset;
import com.example.demo.models.Asset.AssetStatus;
import com.example.demo.models.Asset.AssetType;
import com.example.demo.services.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller - ito ang entry point ng HTTP requests mula sa frontend
// Lahat ng asset-related na API endpoints ay nandito
@RestController                    // Sinasabi na REST controller ito - JSON ang output
@RequestMapping("/api/assets")     // Base URL para sa lahat ng endpoints dito
@RequiredArgsConstructor
@CrossOrigin(origins = "*")        // Para matanggap ang requests mula sa frontend (kahit ibang port)
public class AssetController {

    private final AssetService assetService;

    // POST /api/assets - Mag-add ng bagong asset
    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody AssetRequest request) {
        // @RequestBody - ang JSON body ng request ay automatically na-convert sa AssetRequest object
        Asset asset = assetService.createAsset(request);
        return ResponseEntity.ok(asset); // HTTP 200 + yung naka-save na asset
    }

    // GET /api/assets - Kunin lahat ng assets
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    // GET /api/assets/{id} - Kunin ang isang specific na asset
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        // @PathVariable - kinukuha ang ID mula sa URL (hal. /api/assets/5)
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    // GET /api/assets/status/{status} - Kunin ang assets ayon sa status
    // Hal. GET /api/assets/status/AVAILABLE
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Asset>> getAssetsByStatus(@PathVariable AssetStatus status) {
        return ResponseEntity.ok(assetService.getAssetsByStatus(status));
    }

    // GET /api/assets/type/{type} - Kunin ang assets ayon sa type
    // Hal. GET /api/assets/type/LAPTOP
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Asset>> getAssetsByType(@PathVariable AssetType type) {
        return ResponseEntity.ok(assetService.getAssetsByType(type));
    }

    // GET /api/assets/search?name=laptop - Maghanap ng asset gamit ang name
    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchAssets(@RequestParam String name) {
        // @RequestParam - kinukuha ang value mula sa query parameter ng URL
        return ResponseEntity.ok(assetService.searchAssets(name));
    }

    // PUT /api/assets/{id} - I-update ang impormasyon ng asset
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody AssetRequest request) {
        return ResponseEntity.ok(assetService.updateAsset(id, request));
    }

    // PATCH /api/assets/{id}/status/{status} - I-update lang ang status ng asset
    // Hal. PATCH /api/assets/5/status/UNDER_REPAIR
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Asset> updateAssetStatus(@PathVariable Long id, @PathVariable AssetStatus status) {
        return ResponseEntity.ok(assetService.updateAssetStatus(id, status));
    }

    // DELETE /api/assets/{id} - I-delete ang asset
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Matagumpay na natanggal ang asset!"); // Success message
    }
}
