package com.example.demo.repository;

import com.example.demo.models.Asset;
import com.example.demo.models.Asset.AssetStatus;
import com.example.demo.models.Asset.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository para sa Asset - ang JpaRepository ay nagbibigay na ng basic CRUD operations
// Hindi na kailangan pang isulat ang save(), findAll(), findById(), delete() - auto na yun!
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    // Hanapin ang asset gamit ang serial number (unique identifier)
    Optional<Asset> findBySerialNumber(String serialNumber);

    // Kunin lahat ng assets na may specific na status (hal. lahat ng AVAILABLE)
    List<Asset> findByStatus(AssetStatus status);

    // Kunin lahat ng assets ayon sa type (hal. lahat ng LAPTOP)
    List<Asset> findByType(AssetType type);

    // Kunin lahat ng assets ng specific na brand
    List<Asset> findByBrand(String brand);

    // Hanapin ang asset gamit ang name (case-insensitive, partial match)
    List<Asset> findByNameContainingIgnoreCase(String name);

    // Kunin lahat ng assets na may specific na type at status
    List<Asset> findByTypeAndStatus(AssetType type, AssetStatus status);

    // I-check kung may existing na asset na may ganyang serial number
    boolean existsBySerialNumber(String serialNumber);
}
