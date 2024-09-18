/**
 * The {@code CustomerService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.Customers;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.exceptions.custom.BusinessNotFoundException;
import com.strix_invoice.app.exceptions.custom.CustomersNotFoundException;
import com.strix_invoice.app.exceptions.custom.UserNotFoundException;
import com.strix_invoice.app.model.CustomersModel;
import com.strix_invoice.app.projections.customers.CustomersProjection;
import com.strix_invoice.app.projections.customers.CustomersWithAddressProjection;
import com.strix_invoice.app.repository.CustomersRepository;
import com.strix_invoice.app.utility.Mapper;
import com.strix_invoice.app.utility.Updater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private CustomersRepository customersRepository;

    @Transactional
    public Customers addCustomer(Long businessId, CustomersModel customersModel, Long userId) {

        Business business = businessService.getBusiness(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business Not Found for Id :" + businessId));

        if (!business.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("User with ID " + userId + " does not have permission to create a customer for business with ID " + businessId);
        }

        Customers customers = Mapper.mapCustomersModelToCustomers(customersModel);
        customers.setBillingAddress(Mapper.mapAddressModelToAddress(customersModel.getBillingAddress()));
        customers.setShippingAddress(Mapper.mapAddressModelToAddress(customersModel.getShippingAddress()));
        customers.setBusiness(business);
        customers.setUsersInfo(business.getUsersInfo());

        return customersRepository.save(customers);
    }

    @Transactional
    public Customers updateCustomer(Long customerId, CustomersModel customersModel, Long userId) {
        Customers customers = customersRepository.findById(customerId)
                .orElseThrow(() -> new CustomersNotFoundException("Customer with customerId " + customerId + " Not Found"));

        if (!customers.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("user with ID " + userId + " Do not have access to update this Resource");
        }

        Updater.updateCustomer(customers, customersModel);
        Updater.updateAddress(customers.getBillingAddress(), customersModel.getBillingAddress());
        Updater.updateAddress(customers.getShippingAddress(), customersModel.getShippingAddress());

        return customersRepository.save(customers);
    }

    public CustomersWithAddressProjection retrieveCustomers(Long customerId) {
        CustomersWithAddressProjection customers = customersRepository.retrieveCustomersWithAddressProjection(customerId)
                .orElseThrow(() -> new CustomersNotFoundException("Customer with id " + customerId + " not found."));

        return customers;
    }

    public List<CustomersProjection> retrieveAllCustomers(Long userId) {
        List<CustomersProjection> allCustomersByProjection = customersRepository.findAllCustomersByProjection(userId);
        return allCustomersByProjection;
    }

    public List<CustomersProjection> retrieveAllCustomers(Long businessId, Long userId) {
        Business business = businessService.getBusiness(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business Not for Id : " + businessId));

        if (!business.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("Don't have permission to access this Resource with business id : " + businessId);
        }

        return customersRepository.findAllCustomersByProjectionForBusiness(businessId);
    }

    public List<CustomersWithAddressProjection> retrieveAll() {
        return customersRepository.retrieveAllCustomers();
    }

    /**
     * Deletes a customer by the given customers ID, by verifying requesting user has the necessary permissions.
     *
     * @param customersId the ID of the customer to be deleted
     * @param userId      the ID of the currently authenticated user
     * @throws CustomersNotFoundException if the customer with the given ID is not found
     * @throws AccessDeniedException      if the user does not have permission to delete this customer
     */
    public void deleteCustomers(Long customersId, Long userId) {
        Customers customer = customersRepository.findById(customersId)
                .orElseThrow(() -> new CustomersNotFoundException("Customer with ID " + customersId + " not found"));

        if (!customer.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("User with ID " + userId + " does not have permission to delete this customer.");
        }
        customersRepository.delete(customer);
    }
}
