package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Ito yung entity na kumakatawan sa isang IT asset (hal. laptop, monitor, printer)
@Entity
@Table(name = "assets")
@Data                  // Auto-generate getters, setters, toString (gawa ni Lombok)
@NoArgsConstructor     // Default constructor - kailangan ng JPA
@AllArgsConstructor    // Constructor with all fields
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ang ID
    private Long id;

    // Pangalan ng asset (hal. "Dell Laptop", "HP Monitor")
    @Column(nullable = false)
    private String name;

    // Type ng asset (LAPTOP, DESKTOP, MONITOR, PRINTER, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    // Serial number para ma-identify nang unique ang bawat asset
    @Column(unique = true, nullable = false)
    private String serialNumber;

    // Brand ng asset (hal. "Dell", "HP", "Lenovo")
    private String brand;

    // Model number ng asset
    private String model;

    // Status kung available ba or in-use na ang asset
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    // Petsa kung kailan binili ang asset
    private String purchaseDate;

    // Halaga ng asset nung binili
    private Double purchasePrice;

    // Mga notes o remarks tungkol sa asset
    private String remarks;

    // Enum para sa type ng asset
    public enum AssetType {
        LAPTOP,
        DESKTOP,
        MONITOR,
        PRINTER,
        KEYBOARD,
        MOUSE,
        UPS,
        SERVER,
        NETWORKING,
        OTHER
    }

    // Enum para sa status ng asset
    public enum AssetStatus {
        AVAILABLE,    // Pwede pang hiramin
        IN_USE,       // May gumagamit na
        UNDER_REPAIR, // Nirerepair
        RETIRED,      // Hindi na ginagamit
        LOST          // Nawawala
    }
}
