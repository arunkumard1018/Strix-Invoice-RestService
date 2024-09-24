/**
 * The {@code UserInfoService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service;

import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.exceptions.custom.UserNotFoundException;
import com.strix_invoice.app.projections.usersInfo.UsersInfoProjection;
import com.strix_invoice.app.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    UserInfoRepository userInfoRepository;

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
}
