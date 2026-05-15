package com.example.demo.dto;

import com.example.demo.models.Asset.AssetStatus;
import com.example.demo.models.Asset.AssetType;
import lombok.Data;

// DTO = Data Transfer Object
// Ito yung object na ginagamit para mag-receive ng data mula sa frontend
// Hindi direkta ang entity ang ginagamit para hindi ma-expose ang lahat ng fields
@Data
public class AssetRequest {

    // Required fields - kailangan laging may laman ito
    private String name;          // Pangalan ng asset
    private AssetType type;       // Type ng asset (LAPTOP, MONITOR, etc.)
    private String serialNumber;  // Unique serial number
    private AssetStatus status;   // Status ng asset

    // Optional fields - pwedeng wala
    private String brand;         // Brand (hal. Dell, HP)
    private String model;         // Model number
    private String purchaseDate;  // Petsa ng pagbili
    private Double purchasePrice; // Presyo nung binili
    private String remarks;       // Mga notes
}
