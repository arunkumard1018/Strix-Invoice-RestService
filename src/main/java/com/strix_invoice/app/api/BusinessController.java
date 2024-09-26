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
import com.strix_invoice.app.projections.business.BusinessDataWithAddressProjection;
import com.strix_invoice.app.projections.business.BusinessProjection;
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
import java.util.Optional;
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
    public ResponseEntity business(@Valid @RequestBody BusinessModel businessModel,
                                   @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        System.out.println(businessModel);
        log.info("Request to Create Business By user with ID {} ", userId);

        Business business = businessService.createBusiness(businessModel, userId);

        log.info("User created Successfully with id {} for user {}", business.getId(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Business created successfully.");

    }

    @PutMapping("/business/{businessId}")
    public ResponseEntity updateBusiness(@Valid @RequestBody BusinessModel businessModel,
                                         @PathVariable Long businessId,
                                         @AuthenticationPrincipal UsersPrincipal principal) {

        Long userId = principal.getUserId();
        log.info("Request to update business id {} for user id {}", businessId, userId);

        businessService.updateBusiness(businessId, userId, businessModel);

        log.info("Business with ID {} updated successfully by user {}", businessId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Business updated successfully.");

    }


    @GetMapping("/business")
    public Set<BusinessProjection> retrieveAllBusiness(@AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();

        log.info("Request for Business by user with id {} ", userId);

        Set<BusinessProjection> businessProjections = businessService.retrieveAllBusinessFor(userId);

        log.info("Request for Business by user with id {} Dispatched Successfully.", userId);
        return businessProjections;
    }

    @GetMapping("/business-with-address")
    public List<BusinessDataWithAddressProjection> retrieveAllBusinessWithAddress(@AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request for Business by user with id {} ", userId);

        List<BusinessDataWithAddressProjection> business = businessService.retrieveAllBusinessWithAddress(userId);

        log.info("Request for Business by user with id {} Dispatched Successfully.", userId);
        return business;
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<BusinessDataWithAddressProjection> retrieveBusiness(
            @PathVariable Long businessId,
            @AuthenticationPrincipal UsersPrincipal principal) {

        Long currentUserId = principal.getUserId();
        log.info("Request for Business with business ID {} by user with ID {}", businessId, currentUserId);

        // Fetch the business and address details
        BusinessDataWithAddressProjection businessWithAddress = businessService.getBusinessWithAddress(businessId, currentUserId);

        log.info("Business info dispatched successfully for Business ID {}", businessId);
        // Return 200 OK response with the business data
        return ResponseEntity.ok(businessWithAddress);
    }

    @DeleteMapping("/business/{businessId}")
    public ResponseEntity deleteBusiness(@PathVariable Long businessId,@AuthenticationPrincipal UsersPrincipal principal){
        Long userId = principal.getUserId();
        log.info("Request to Delete business with ID {} by user with ID {}.",businessId,userId);

        businessService.deleteBusiness(businessId,userId);

        log.info("user with ID {} Successfully Deleted business with ID {}.",userId,businessId);
        return ResponseEntity.status(HttpStatus.OK).body("deleted Successfully");
    }

    @GetMapping("/business-for/{businessId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusinessDataWithAddressProjection> business(@PathVariable Long businessId) {
        BusinessDataWithAddressProjection business = businessService.getBusinessFor(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business Details Not Found For id :" + businessId));

        return ResponseEntity.status(HttpStatus.OK).body(business);
    }

    @GetMapping("/all-business")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Business> business(@AuthenticationPrincipal UsersPrincipal usersPrincipal) {
        return businessService.retrieveAllBusiness(usersPrincipal.getUserId());
    }

}

