package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

// DTO para sa pag-assign ng asset sa isang user
// Ginagamit ito pag may empleyado na tatanggap ng IT equipment
@Data
public class AssignmentRequest {

    // ID ng asset na ia-assign - kailangan ito para malaman kung aling asset
    private Long assetId;

    // ID ng user na tatanggap ng asset
    private Long userId;

    // Petsa ng pag-assign (kung null, automatic na today ang gagamitin)
    private LocalDate assignedDate;

    // Pangalan ng admin na nag-approve ng assignment
    private String assignedBy;

    // Mga notes o remarks (hal. "For project use lang to")
    private String notes;
}
