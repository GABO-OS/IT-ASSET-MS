package com.example.demo.services;

import com.example.demo.dto.AssetRequest;
import com.example.demo.models.Asset;
import com.example.demo.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("null")
public class AssetService {

    private final AssetRepository assetRepository;

    // Manual Constructor
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset createAsset(AssetRequest request) {
        if (assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new RuntimeException("May existing na asset na may serial number na: " + request.getSerialNumber());
        }

        Asset asset = new Asset();
        asset.setName(request.getName());
        asset.setType(request.getType());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setBrand(request.getBrand());
        asset.setModel(request.getModel());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setRemarks(request.getRemarks());
        asset.setStatus(request.getStatus() != null ? request.getStatus() : Asset.AssetStatus.AVAILABLE);

        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang asset na may ID: " + id));
    }

    public List<Asset> getAssetsByStatus(Asset.AssetStatus status) {
        return assetRepository.findByStatus(status);
    }

    public List<Asset> getAssetsByType(Asset.AssetType type) {
        return assetRepository.findByType(type);
    }

    public List<Asset> searchAssets(String name) {
        return assetRepository.findByNameContainingIgnoreCase(name);
    }

    public Asset updateAsset(Long id, AssetRequest request) {
        Asset asset = getAssetById(id);

        if (!asset.getSerialNumber().equals(request.getSerialNumber())
                && assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new RuntimeException("May ibang asset na na may serial number na: " + request.getSerialNumber());
        }

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

    public Asset updateAssetStatus(Long id, Asset.AssetStatus status) {
        Asset asset = getAssetById(id);
        asset.setStatus(status);
        return assetRepository.save(asset);
    }

    public void deleteAsset(Long id) {
        Asset asset = getAssetById(id);
        assetRepository.delete(asset);
    }
}
