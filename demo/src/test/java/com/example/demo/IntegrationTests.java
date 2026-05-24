package com.example.demo;

import com.example.demo.dto.AssetRequest;
import com.example.demo.dto.AssignmentRequest;
import com.example.demo.dto.MaintenanceRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.models.Asset;
import com.example.demo.models.AssetAssignment;
import com.example.demo.models.AssetMaintenance;
import com.example.demo.models.User;
import com.example.demo.services.AssetAssignmentService;
import com.example.demo.services.AssetMaintenanceService;
import com.example.demo.services.AssetService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test suite verifying core IT Asset operational modules and status integrations:
 * - Borrowed laptop assigned to employee
 * - Maintenance updates status automatically
 * - Assignment of unavailable devices blocked
 */
@SpringBootTest
@Transactional
public class IntegrationTests {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssetAssignmentService assignmentService;

    @Autowired
    private AssetMaintenanceService maintenanceService;

    private Asset testAsset;
    private User testUser;

    @BeforeEach
    public void setup() {
        // Register a test asset
        AssetRequest assetReq = new AssetRequest();
        assetReq.setName("Test Dell Laptop");
        assetReq.setType(Asset.AssetType.LAPTOP);
        assetReq.setSerialNumber("INTEG-TEST-SN-123");
        assetReq.setBrand("Dell");
        assetReq.setModel("Latitude 5420");
        assetReq.setStatus(Asset.AssetStatus.AVAILABLE);
        testAsset = assetService.createAsset(assetReq);

        // Register a test user (employee)
        UserRequest userReq = new UserRequest();
        userReq.setFullName("Maria Clara");
        userReq.setEmail("maria.clara@company.com");
        userReq.setDepartment("Engineering");
        userReq.setRole(com.example.demo.models.User.UserRole.EMPLOYEE);
        testUser = userService.createUser(userReq);
    }

    /**
     * Requirement: borrowed laptop assigned to employee
     * Verifies successful assignment of an available asset, generating an active assignment record and changing status to IN_USE.
     */
    @Test
    public void testBorrowedLaptopAssignedToEmployee() {
        // Assert initial available state
        assertEquals(Asset.AssetStatus.AVAILABLE, testAsset.getStatus());

        // Create assignment request
        AssignmentRequest assignReq = new AssignmentRequest();
        assignReq.setAssetId(testAsset.getId());
        assignReq.setUserId(testUser.getId());
        assignReq.setAssignedBy("System Test Officer");
        assignReq.setNotes("First assignment Integration Test.");

        // Execute assignment
        AssetAssignment assignment = assignmentService.assignAsset(assignReq);

        // Verify assignment details
        assertNotNull(assignment.getId());
        assertEquals(testUser.getId(), assignment.getUser().getId());
        assertEquals(testAsset.getId(), assignment.getAsset().getId());
        assertEquals(AssetAssignment.AssignmentStatus.ACTIVE, assignment.getStatus());

        // Verify asset status transitioned to IN_USE
        Asset updatedAsset = assetService.getAssetById(testAsset.getId());
        assertEquals(Asset.AssetStatus.IN_USE, updatedAsset.getStatus());
    }

    /**
     * Requirement: maintenance updates status
     * Verifies that registering an asset for maintenance updates status to UNDER_REPAIR, and completing updates status back to AVAILABLE.
     */
    @Test
    public void testMaintenanceUpdatesStatus() {
        // Assert initial available state
        assertEquals(Asset.AssetStatus.AVAILABLE, testAsset.getStatus());

        // Register asset for maintenance
        MaintenanceRequest maintReq = new MaintenanceRequest();
        maintReq.setAssetId(testAsset.getId());
        maintReq.setIssue("Battery replacement required");
        maintReq.setCost(75.50);

        AssetMaintenance maintenance = maintenanceService.registerMaintenance(maintReq);

        // Verify maintenance log state
        assertNotNull(maintenance.getId());
        assertEquals(AssetMaintenance.MaintenanceStatus.PENDING, maintenance.getStatus());

        // Verify asset status transitioned automatically to UNDER_REPAIR
        Asset updatedAsset = assetService.getAssetById(testAsset.getId());
        assertEquals(Asset.AssetStatus.UNDER_REPAIR, updatedAsset.getStatus());

        // Complete the repair
        AssetMaintenance resolved = maintenanceService.resolveMaintenance(
                maintenance.getId(),
                AssetMaintenance.MaintenanceStatus.COMPLETED,
                "New battery installed successfully.",
                75.50
        );

        // Verify log is COMPLETED
        assertEquals(AssetMaintenance.MaintenanceStatus.COMPLETED, resolved.getStatus());

        // Verify asset status reverted automatically to AVAILABLE
        Asset resolvedAsset = assetService.getAssetById(testAsset.getId());
        assertEquals(Asset.AssetStatus.AVAILABLE, resolvedAsset.getStatus());
    }

    /**
     * Requirement: unavailable devices blocked
     * Verifies that attempting to borrow an asset with a status other than AVAILABLE (e.g., UNDER_REPAIR) is blocked and throws exceptions.
     */
    @Test
    public void testUnavailableDevicesBlocked() {
        // Place asset into maintenance (status: UNDER_REPAIR)
        MaintenanceRequest maintReq = new MaintenanceRequest();
        maintReq.setAssetId(testAsset.getId());
        maintReq.setIssue("Broken screen");
        maintenanceService.registerMaintenance(maintReq);

        // Assert asset status is now UNDER_REPAIR
        Asset repairAsset = assetService.getAssetById(testAsset.getId());
        assertEquals(Asset.AssetStatus.UNDER_REPAIR, repairAsset.getStatus());

        // Try to assign this under-repair asset
        AssignmentRequest assignReq = new AssignmentRequest();
        assignReq.setAssetId(testAsset.getId());
        assignReq.setUserId(testUser.getId());
        assignReq.setAssignedBy("System Test Officer");

        // Verify borrow action is blocked and throws exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assignmentService.assignAsset(assignReq);
        });

        // Verify exception mentions asset availability/status
        assertTrue(exception.getMessage().contains("kasi ang status nito ay: UNDER_REPAIR") 
                || exception.getMessage().contains("status") 
                || exception.getMessage().contains("assign"));
    }
}
