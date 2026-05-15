package com.example.demo.dto;

import com.example.demo.models.Asset.AssetStatus;
import com.example.demo.models.Asset.AssetType;

public class AssetRequest {
    private String name;
    private AssetType type;
    private String serialNumber;
    private AssetStatus status;
    private String brand;
    private String model;
    private String purchaseDate;
    private Double purchasePrice;
    private String remarks;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public AssetType getType() { return type; }
    public void setType(AssetType type) { this.type = type; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public AssetStatus getStatus() { return status; }
    public void setStatus(AssetStatus status) { this.status = status; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public Double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
