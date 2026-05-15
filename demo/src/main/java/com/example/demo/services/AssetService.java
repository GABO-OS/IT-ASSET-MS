package com.example.demo.services;

import com.example.demo.dto.AssetRequest;
import com.example.demo.models.Asset;
import com.example.demo.models.Asset.AssetStatus;
import com.example.demo.models.Asset.AssetType;
import com.example.demo.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Service layer - dito nakalagay ang business logic
// Controller -> Service -> Repository ang flow ng data
@Service
@RequiredArgsConstructor // Lombok: auto-generate constructor para sa final fields (para sa dependency injection)
public class AssetService {

    // I-inject ang repository para makausap ang database
    private final AssetRepository assetRepository;

    // ===================== CREATE =====================

    // Mag-add ng bagong asset sa system
    public Asset createAsset(AssetRequest request) {
        // I-check muna kung may ganyang serial number na - para walang duplicate
        if (assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new RuntimeException("May existing na asset na may serial number na: " + request.getSerialNumber());
        }

        // Gumawa ng bagong Asset object at i-populate ang mga fields
        Asset asset = new Asset();
        asset.setName(request.getName());
        asset.setType(request.getType());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setRemarks(request.getRemarks());

        // Kung walang status na ibinigay, default na AVAILABLE
        asset.setStatus(request.getStatus() != null ? request.getStatus() : AssetStatus.AVAILABLE);

        // I-save sa database at ibalik ang naka-save na asset
        return assetRepository.save(asset);
    }

    // ===================== READ =====================

    // Kunin lahat ng assets (para sa asset list/table ng admin)
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // Hanapin ang isang asset gamit ang ID
    public Asset getAssetById(Long id) {
        // Kung hindi mahanap, mag-throw ng error - para malaman ng frontend na invalid ang ID
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang asset na may ID: " + id));
    }

    // Kunin lahat ng assets ayon sa status (hal. lahat ng AVAILABLE)
    public List<Asset> getAssetsByStatus(AssetStatus status) {
        return assetRepository.findByStatus(status);
    }

    // Kunin lahat ng assets ayon sa type (hal. lahat ng LAPTOP)
    public List<Asset> getAssetsByType(AssetType type) {
        return assetRepository.findByType(type);
    }

    // Maghanap ng asset gamit ang name (partial search)
    public List<Asset> searchAssets(String name) {
        return assetRepository.findByNameContainingIgnoreCase(name);
    }

    // ===================== UPDATE =====================

    // I-update ang impormasyon ng isang asset
    public Asset updateAsset(Long id, AssetRequest request) {
        // Hanapin muna ang asset - kung wala, mag-error
        Asset asset = getAssetById(id);

        // I-check kung may ibang asset na may ganyang serial number
        // (basta hindi yung sarili niya)
        if (!asset.getSerialNumber().equals(request.getSerialNumber())
                && assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new RuntimeException("May ibang asset na na may serial number na: " + request.getSerialNumber());
        }

        // I-update ang mga fields ng asset
        asset.setName(request.getName());
        asset.setType(request.getType());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setRemarks(request.getRemarks());

        if (request.getStatus() != null) {
            asset.setStatus(request.getStatus());
        }

        return assetRepository.save(asset);
    }

    // I-update lang ang status ng asset (para hindi kailangan i-update ang lahat ng fields)
    public Asset updateAssetStatus(Long id, AssetStatus status) {
        Asset asset = getAssetById(id);
        asset.setStatus(status);
        return assetRepository.save(asset);
    }

    // ===================== DELETE =====================

    // I-delete ang asset (mag-ingat - baka may assignment pa ito!)
    public void deleteAsset(Long id) {
        // I-check muna kung existing ang asset bago i-delete
        Asset asset = getAssetById(id);
        assetRepository.delete(asset);
    }
}
