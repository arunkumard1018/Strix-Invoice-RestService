/**
 * The {@code BusinessController$} class represents Functionalities
 *
 * @author ArunKumar D
 */
package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.exceptions.custom.BusinessNotFoundException;
import com.strix_invoice.app.exceptions.custom.ResourceNotFoundException;
import com.strix_invoice.app.exceptions.custom.UserNotFoundException;
import com.strix_invoice.app.model.BusinessModel;
import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.service.BusinessService;
import com.strix_invoice.app.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
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
    public ResponseEntity<?> updateBusiness(@Valid @RequestBody BusinessModel businessModel,
                                      @PathVariable Long businessId, @AuthenticationPrincipal UsersPrincipal principal) {

        Long userId = principal.getUserId();
        try {
            log.info("Request to update business id {} for user id {}", businessId, principal.getUserId());

            businessService.updateBusiness(businessId, userId, businessModel);
            log.info("Business with ID {} updated successfully by user {}", businessId, userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Business updated successfully.");

        } catch (UserNotFoundException ex) {
            log.error("User with ID {} not found", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        } catch (BusinessNotFoundException ex) {
            log.error("Business with ID {} not found", businessId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        }catch (AccessDeniedException ex){
            log.error("Unauthorized access: User {} tried to update business {}", userId, businessId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());

        }catch (Exception ex) {
            log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }


    @GetMapping("/business")
    public Set<Business> businessForUsers(@AuthenticationPrincipal UsersPrincipal principal) {
        return businessService.retrieveAllBusinessFor(principal.getUserId());
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

