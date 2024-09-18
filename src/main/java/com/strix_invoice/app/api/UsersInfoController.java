/**
 * The {@code UsersInfoController$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
public class UsersInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/users-info/{id}")
    public ResponseEntity getUserInfo(@PathVariable Long id){
        log.info("Request for User Info with id {}",id);
        UsersInfo usersInfo = userInfoService.findUser(id).get();
        System.out.println(usersInfo.getBusinesses());
        Set<Business> businesses = usersInfo.getBusinesses();
        log.info("UserInfo Fetched Successfully with id {}",usersInfo);
        return ResponseEntity.ok(businesses);
    }
}
