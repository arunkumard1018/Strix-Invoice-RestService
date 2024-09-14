/**
 * The {@code BusinessController$} class represents Functionalities
 *
 * @author ArunKumar D
 */
package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.exceptions.custom.ResourceNotFoundException;
import com.strix_invoice.app.model.BusinessModel;
import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.service.BusinessService;
import com.strix_invoice.app.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("${api.base-path}")
public class BusinessController {
    @Autowired
    private BusinessService businessService;
    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/business")
    public ResponseEntity<?> business(@Valid @RequestBody BusinessModel businessModel, @AuthenticationPrincipal UsersPrincipal principal) {
        try {
            businessService.createBusiness(businessModel, principal.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Business created successfully.");
        } catch (Exception ex) {
            // Add The Log for the exception for debugging (add a logger in production)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/business/{businessId}")
    public ResponseEntity<?> Business(@Valid @RequestBody BusinessModel businessModel,
                                      @PathVariable long businessId, @AuthenticationPrincipal UsersPrincipal principal) {
        try {
            businessService.updateBusiness(businessModel, principal.getUserId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Business created successfully.");
        } catch (Exception ex) {
            // Add The Log for the exception for debugging (add a logger in production)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }

    @GetMapping("/business")
    public Set<Business> businessForUsers(@AuthenticationPrincipal UsersPrincipal principal) {
        return businessService.retriveAllBusinessFor(principal.getUserId());
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<Business> businessForUser(@PathVariable Long businessId, @AuthenticationPrincipal UsersPrincipal principal) {
        Long currentUserId = principal.getUserId();
        Business business = businessService.getBusiness(businessId).orElseThrow(() ->
                new ResourceNotFoundException("Business Details Not Found For id :" + businessId));

        if (!business.getUsersInfo().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to access this business Resource");
        }
        return ResponseEntity.status(HttpStatus.OK).body(business);
    }

    @GetMapping("/business-for/{businessId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Business> business(@PathVariable Long businessId) {
        Business business = businessService.getBusiness(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business Details Not Found For id :" + businessId));

        return ResponseEntity.status(HttpStatus.OK).body(business);
    }

    @GetMapping("/all-business")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Business> business(@AuthenticationPrincipal UsersPrincipal usersPrincipal) {
        return businessService.retrieveAllBusiness(usersPrincipal.getUserId());
    }

}

