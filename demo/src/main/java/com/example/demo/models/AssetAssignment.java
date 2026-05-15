package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

// Ito yung bridge/linking table - sino may hawak ng anong asset at kailan
@Entity
@Table(name = "asset_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ang asset na ina-assign - ManyToOne kasi isang asset = isang assignment lang at a time
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false) // Foreign key column sa database
    private Asset asset;

    // Ang user na tatanggap ng asset
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column sa database
    private User user;

    // Petsa kung kailan na-assign ang asset
    @Column(nullable = false)
    private LocalDate assignedDate;

    // Petsa kung kailan na-return ang asset (null kung hindi pa nire-return)
    private LocalDate returnedDate;

    // Status ng assignment - kung active pa ba or naibalik na
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    // Sino nag-assign (pangalan ng admin na nag-process)
    private String assignedBy;

    // Notes o remarks tungkol sa assignment
    private String notes;

    // Enum para sa status ng assignment
    public enum AssignmentStatus {
        ACTIVE,   // Ginagamit pa - hindi pa naibalik
        RETURNED, // Naibalik na ang asset
        LOST      // Nawala ang asset habang nasa user
    }
}
