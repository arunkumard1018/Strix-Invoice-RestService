/**
 * The {@code UserInfoService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.exceptions.custom.BusinessNotFoundException;
import com.strix_invoice.app.exceptions.custom.UserNotFoundException;
import com.strix_invoice.app.projections.usersInfo.UsersInfoProjection;
import com.strix_invoice.app.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private BusinessService businessService;

    public void createUser(UsersInfo usersInfo){
        userInfoRepository.save(usersInfo);
    }

    public Optional<UsersInfo> findUser(Long userId) {
        return userInfoRepository.findById(userId);
    }

    public UsersInfoProjection findByUsersInfoProjection(Long userId) {
        UsersInfoProjection users_info = userInfoRepository.findByUsersInfoProjection(userId)
                .orElseThrow(() -> new UserNotFoundException("Users Info Not Exists"));
        return users_info;
    }

    @Transactional
    public void updateActiveBusiness(Long businessId, Long userId) {
        Business business = businessService.findBusiness(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business with Id " + businessId + " not found"));

        if(!business.getUsersInfo().getId().equals(userId)){
            throw new AccessDeniedException("user with id "+userId+" does not have access to business resource");
        }

        UsersInfo usersInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Info Not found for Id "+userId));
        usersInfo.setActiveBusiness(business);

        userInfoRepository.save(usersInfo);
    }
}
