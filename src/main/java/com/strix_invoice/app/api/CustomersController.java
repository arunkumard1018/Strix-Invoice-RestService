/**
 * The {@code CustomersController$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.Customers;
import com.strix_invoice.app.exceptions.custom.BusinessNotFoundException;
import com.strix_invoice.app.exceptions.custom.UserNotFoundException;
import com.strix_invoice.app.model.CustomersModel;
import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.projections.customers.CustomersProjection;
import com.strix_invoice.app.projections.customers.CustomersWithAddressProjection;
import com.strix_invoice.app.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("${api.base-path}")
public class CustomersController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("business/{businessId}/customers")
    public ResponseEntity createCustomer(@PathVariable Long businessId,
                                         @RequestBody CustomersModel customersModel,
                                         @AuthenticationPrincipal UsersPrincipal principal) {

        Long userId = principal.getUserId();
        log.info("Request to Create Customer for business id {} for user id {}", businessId, userId);

        Customers customers = customerService.addCustomer(businessId, customersModel, userId);

        log.info("Customer with id {} Created Successfully for Business id {} by userId {}", customers.getId(), businessId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer Created Successfully");

    }

    @GetMapping("business/{businessId}/customers")
    public ResponseEntity<List<CustomersProjection>> retrieveCustomers(@PathVariable Long businessId, @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request for Customers Details for Business id-{} by user with id - {}", businessId, userId);

        List<CustomersProjection> customers = customerService.retrieveAllCustomers(businessId, userId);

        log.info("Successfully Dispatched the Request for Customers Details for Business id-{} by user with id - {}", businessId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @PutMapping("/customers/{customersId}")
    public ResponseEntity updateCustomer(@PathVariable Long customersId, @RequestBody CustomersModel customersModel, @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request to Update Customer for customers id {} by user with id {}", customersId, userId);

        Customers customers = customerService.updateCustomer(customersId, customersModel, userId);

        log.info("Successfully Updated Customer with id {} by  user with id {}", customersId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("Customer Updated Successfully");
    }

    @GetMapping("/customers/{customersId}")
    public ResponseEntity customers(@PathVariable Long customersId, @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request for accessing Customers with id {} by user with ID {}", customersId, userId);

        CustomersWithAddressProjection customers = customerService.retrieveCustomers(customersId);

        if (!principal.isAdmin() && !customers.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("user with ID " + userId + " does not have permission to Access this Customer Resource with ID " + customersId);
        }

        log.info("Request Dispatched Successfully for accessing Customers with id {} by user with id {}", customersId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/customers")
    public ResponseEntity retrieveAllCustomers(@AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request For Customer Details by User with id {}", userId);

        List<CustomersProjection> customers = customerService.retrieveAllCustomers(userId);

        log.info("Dispatching Customers info user with id {} ", userId);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }


    /**
     * Deletes a customer with the specified customers ID.
     *
     * <p>This method handles the deletion of a customer by a user. It first verifies if the user
     * has the appropriate permissions and if the customer exists in the business context.</p>
     *
     * @param customersId The ID of the customer to be deleted.
     * @param principal   The authenticated user attempting the deletion.
     * @return A {@link ResponseEntity} indicating the result of the deletion operation.
     * <p>
     * Expected HTTP Status Codes:
     * - 204 NO_CONTENT: If the customer is deleted successfully.
     * - 403 FORBIDDEN: If the user does not have the necessary permissions to delete the customer.
     * - 404 NOT_FOUND: If the user or business related to the customer is not found.
     * - 500 INTERNAL_SERVER_ERROR: For any unexpected server errors.
     * @throws AccessDeniedException     If the user lacks permission to delete the customer.
     * @throws UserNotFoundException     If the user does not exist in the system.
     * @throws BusinessNotFoundException If the business related to the customer is not found.
     *                                   <p>
     *                                   Note :
     */
    @DeleteMapping("/customers/{customersId}")
    public ResponseEntity deleteCustomers(@PathVariable Long customersId, @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request received to delete Customer with ID {} by User with ID {}", customersId, userId);

        customerService.deleteCustomers(customersId, userId);

        log.info("Customer with ID {} deleted successfully by User with ID {}", customersId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer deleted successfully.");

    }


    @GetMapping("/admin/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomersWithAddressProjection> getAllCustomers() {
        return customerService.retrieveAll();
    }
}
