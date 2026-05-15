package com.example.demo.controller;

import com.example.demo.dto.AssignmentRequest;
import com.example.demo.models.AssetAssignment;
import com.example.demo.services.AssetAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssetAssignmentController {

    private final AssetAssignmentService assignmentService;

    // Manual Constructor (Replacing @RequiredArgsConstructor)
    public AssetAssignmentController(AssetAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<AssetAssignment> assignAsset(@RequestBody AssignmentRequest request) {
        return ResponseEntity.ok(assignmentService.assignAsset(request));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<AssetAssignment> returnAsset(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.returnAsset(id));
    }

    @PatchMapping("/{id}/lost")
    public ResponseEntity<AssetAssignment> markAsLost(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.markAsLost(id));
    }

    @GetMapping
    public ResponseEntity<List<AssetAssignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AssetAssignment>> getActiveAssignments() {
        return ResponseEntity.ok(assignmentService.getActiveAssignments());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssetAssignment>> getAssignmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByUser(userId));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetAssignment>> getAssignmentsByAsset(@PathVariable Long assetId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByAsset(assetId));
    }
}
