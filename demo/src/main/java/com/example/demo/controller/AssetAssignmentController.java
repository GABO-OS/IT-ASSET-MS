package com.example.demo.controller;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.models.AssetAssignment;
import com.example.demo.services.AssetAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller para sa Asset Assignment - ito ang puso ng IT Asset Management System
// Dito nangyayari ang pag-assign at pag-return ng mga IT assets
@RestController
@RequestMapping("/api/assignments")  // Base URL para sa assignment endpoints
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssetAssignmentController {

    private final AssetAssignmentService assignmentService;

    // POST /api/assignments/assign - Mag-assign ng asset sa empleyado
    // Hal. Ibibigay ang laptop sa bagong empleyado
    @PostMapping("/assign")
    public ResponseEntity<AssetAssignment> assignAsset(@RequestBody AssignmentRequest request) {
        AssetAssignment assignment = assignmentService.assignAsset(request);
        return ResponseEntity.ok(assignment);
    }

    // PATCH /api/assignments/{id}/return - I-process ang pagbabalik ng asset
    // Hal. Ibinalik ng empleyado ang laptop
    @PatchMapping("/{id}/return")
    public ResponseEntity<AssetAssignment> returnAsset(@PathVariable Long id) {
        AssetAssignment assignment = assignmentService.returnAsset(id);
        return ResponseEntity.ok(assignment);
    }

    // PATCH /api/assignments/{id}/lost - I-mark ang asset na nawala
    // Gamitin ito kung nawala ang asset ng empleyado
    @PatchMapping("/{id}/lost")
    public ResponseEntity<AssetAssignment> markAsLost(@PathVariable Long id) {
        AssetAssignment assignment = assignmentService.markAsLost(id);
        return ResponseEntity.ok(assignment);
    }

    // GET /api/assignments - Kunin lahat ng assignments (buong history)
    @GetMapping
    public ResponseEntity<List<AssetAssignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    // GET /api/assignments/active - Kunin lahat ng ACTIVE assignments
    // Magagamit ito para makita kung sino ang may hawak ng mga assets ngayon
    @GetMapping("/active")
    public ResponseEntity<List<AssetAssignment>> getActiveAssignments() {
        return ResponseEntity.ok(assignmentService.getActiveAssignments());
    }

    // GET /api/assignments/{id} - Kunin ang isang specific na assignment
    @GetMapping("/{id}")
    public ResponseEntity<AssetAssignment> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    // GET /api/assignments/user/{userId} - Kunin lahat ng assignments ng isang user
    // Magagamit para makita kung anong mga assets ang na-assign sa isang empleyado
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssetAssignment>> getAssignmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByUser(userId));
    }

    // GET /api/assignments/asset/{assetId} - Kunin ang history ng isang asset
    // Makikita dito kung sino-sino na ang nagamit ng asset na ito
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetAssignment>> getAssignmentsByAsset(@PathVariable Long assetId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByAsset(assetId));
    }
}
