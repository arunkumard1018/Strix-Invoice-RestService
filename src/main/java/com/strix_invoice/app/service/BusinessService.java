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
import com.strix_invoice.app.repository.BusinessRepository;
import com.strix_invoice.app.repository.UserInfoRepository;
import com.strix_invoice.app.utility.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Business> retrieveAllBusiness(Long userId) {
        List<Business> all = businessRepository.findAll();
        return all;
    }

    @Transactional
    public void createBusiness(BusinessModel businessModel, Long userId) {
        Optional<UsersInfo> usersInfo = userInfoRepository.findById(userId);
        Business business = Mapper.mapBusinessModelToBusiness(businessModel);
        business.setUsersInfo(usersInfo.get());
        business.setAddress(Mapper.mapBusinessModelToAddress(businessModel));
        businessRepository.save(business);
    }


    public void updateBusiness(Long businessId, Long userId, BusinessModel businessModel) {

        UsersInfo usersInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> {
                    return new UserNotFoundException("User Resource Not Found");
                });

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> {
                    return new BusinessNotFoundException("Business Resource Not Found");
                });

        if (!business.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have access to update this business.");
        }

        Address address = business.getAddress();
        business.setName(businessModel.getName());
        business.setGstin(businessModel.getGstin());
        business.setHsn(businessModel.getHsn());
        business.setStateCode(businessModel.getStateCode());

        address.setAddress(businessModel.getAddress());
        address.setCity(businessModel.getCity());
        address.setState(businessModel.getState());
        address.setZip(businessModel.getZip());

        businessRepository.save(business);
    }

    public Set<Business> retrieveAllBusinessFor(Long userId) {
        Set<Business> businesses = businessRepository.findByUserId(userId);
        businesses.forEach(items -> System.out.print(items));
        return businesses;
    }


    public Optional<Business> getBusiness(Long businessId) {
        Optional<Business> business = businessRepository.findById(businessId);
        return business;
    }


}
