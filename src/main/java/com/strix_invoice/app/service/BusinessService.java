/**
 * The {@code BusinessService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.model.BusinessModel;
import com.strix_invoice.app.repository.BusinessRepository;
import com.strix_invoice.app.repository.UserInfoRepository;
import com.strix_invoice.app.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void updateBusiness(BusinessModel businessModel, Long userId) {
//        Optional<UsersInfo> usersInfo = userInfoRepository.findById(userId)
//                .orElseThrow();
        Business business = Mapper.mapBusinessModelToBusiness(businessModel);
        business.setUsersInfo(usersInfo.get());
        business.setAddress(Mapper.mapBusinessModelToAddress(businessModel));
        businessRepository.save(business);
    }

    public Set<Business> retriveAllBusinessFor(Long userId) {
        Set<Business> businesses = businessRepository.findByUserId(userId);
        businesses.forEach(items -> System.out.print(items));
        return businesses;
    }


    public Optional<Business> getBusiness(Long businessId) {
        Optional<Business> business = businessRepository.findById(businessId);
        return business;
    }


}
