/**
 * The {@code BusinessService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service;

import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.exceptions.custom.BusinessNotFoundException;
import com.strix_invoice.app.exceptions.custom.UserNotFoundException;
import com.strix_invoice.app.model.BusinessModel;
import com.strix_invoice.app.projections.business.BusinessDataWithAddressProjection;
import com.strix_invoice.app.projections.business.BusinessInfoProjection;
import com.strix_invoice.app.projections.business.BusinessProjection;
import com.strix_invoice.app.repository.BusinessRepository;
import com.strix_invoice.app.repository.CustomersRepository;
import com.strix_invoice.app.repository.UserInfoRepository;
import com.strix_invoice.app.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CustomersRepository customersRepository;


    @Transactional
    public Business createBusiness(BusinessModel businessModel, Long userId) {
        UsersInfo usersInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));

        Business business = Mapper.mapBusinessModelToBusiness(businessModel);
        business.setUsersInfo(usersInfo);
        business.setAddress(Mapper.mapBusinessModelToAddress(businessModel));
        return businessRepository.save(business);
    }

    @Transactional
    public Business updateBusiness(Long businessId, Long userId, BusinessModel businessModel) {

        UsersInfo usersInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> {
                    return new UserNotFoundException("User Resource with id" + userId + " not found.");
                });

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> {
                    return new BusinessNotFoundException("Business Resource with id " + businessId + "  not found.");
                });

        if (!business.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("User with ID " + userId + " does not have permission to update the business resource with ID " + businessId + ".");
        }

        Address address = business.getAddress();
        business.setName(businessModel.getName());
        business.setGstin(businessModel.getGstin());
        business.setHsn(businessModel.getHsn());
        business.setStateCode(businessModel.getStateCode());
        business.setInvoicePrefix(businessModel.getInvoicePrefix());
        business.setBusinessType(businessModel.getBusinessType());

        address.setAddress(businessModel.getAddress());
        address.setCity(businessModel.getCity());
        address.setState(businessModel.getState());
        address.setZip(businessModel.getZip());

        Business updatedBusiness = businessRepository.save(business);
        return updatedBusiness;
    }

    public Set<BusinessProjection> retrieveAllBusinessFor(Long userId) {
        Set<BusinessProjection> businesses = businessRepository.findBusinessProjectionByUserId(userId);
        return businesses;
    }

    /* Duplicate for getBusinessWithAddress */
    public Optional<BusinessDataWithAddressProjection> getBusinessFor(Long businessId) {
        Optional<BusinessDataWithAddressProjection> business = businessRepository.findBusinessWithAddressById(businessId);
        return business;
    }

    public Optional<Business> getBusiness(Long businessId) {
        Optional<Business> business = businessRepository.findById(businessId);
        return business;
    }

    public BusinessDataWithAddressProjection getBusinessWithAddress(Long businessId, Long currentUserId) {
        Optional<BusinessDataWithAddressProjection> business = businessRepository.findBusinessWithAddressById(businessId);

        if (!business.isPresent()) {
            throw new BusinessNotFoundException("Business with ID " + businessId + " not found.");
        }

        if (!business.get().getUsersInfo().getId().equals(currentUserId)) {
            throw new AccessDeniedException("User with ID " + currentUserId + " does not have permission to access the business resource with ID " + businessId + ".");
        }

        return business.get();
    }

    public List<BusinessDataWithAddressProjection> retrieveAllBusinessWithAddress(Long userId) {
        List<BusinessDataWithAddressProjection> allBusinessesWithAddress = businessRepository.findAllBusinessesWithAddress();
        return allBusinessesWithAddress;
    }

    public List<Business> retrieveAllBusiness(Long userId) {
        List<Business> all = businessRepository.findAll();
        return all;
    }

    @Transactional
    public void deleteBusiness(Long businessId, Long userId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with ID " + businessId + " not found."));

        if(!business.getUsersInfo().getId().equals(userId)){
            throw new AccessDeniedException("Don't Have Permission to Access Delete This Resource");
        }

        customersRepository.setBusinessIdToNullForBusiness(businessId);
        businessRepository.delete(business);
    }

    public Optional<BusinessInfoProjection> findBusinessInfo(Long businessId) {
        Optional<BusinessInfoProjection> businessProjection =
                businessRepository.findByBusinessProjection(businessId);
        return businessProjection;
    }

    public Optional<Business> findBusiness(Long businessID) {
        return businessRepository.findById(businessID);
    }

}
