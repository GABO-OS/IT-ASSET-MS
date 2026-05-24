package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity to log and track maintenance / repair history of company hardware.
 */
@Entity
@Table(name = "asset_maintenances")
public class AssetMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private String issue;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private Double cost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status;

    private String remarks;

    public AssetMaintenance() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public enum MaintenanceStatus {
        PENDING, COMPLETED, IRREPARABLE
    }
}
