package com.example.demo.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    private String brand;
    private String model;
    private String purchaseDate;
    private Double purchasePrice;
    private String remarks;

    // ADDED @JsonIgnore: Pipigilan nito ang circular reference para hindi mag-loop ang JSON
    @JsonIgnore
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<AssetAssignment> assignments;

    public Asset() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public List<AssetAssignment> getAssignments() { return assignments; }
    public void setAssignments(List<AssetAssignment> assignments) { this.assignments = assignments; }

    public enum AssetType {
        LAPTOP, MONITOR, MOUSE, KEYBOARD, PRINTER, OTHERS
    }

    public enum AssetStatus {
        AVAILABLE, IN_USE, UNDER_REPAIR, LOST, DISPOSED
    }
}
