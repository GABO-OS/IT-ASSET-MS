package com.example.demo.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating and resolving asset repairs.
 */
public class MaintenanceRequest {
    private Long assetId;
    private String issue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double cost;
    private String remarks;

    public MaintenanceRequest() {}

    // Getters and Setters
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
