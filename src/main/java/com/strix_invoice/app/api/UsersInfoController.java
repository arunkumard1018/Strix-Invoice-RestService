/**
 * The {@code UsersInfoController$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.projections.usersInfo.UsersInfoProjection;
import com.strix_invoice.app.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("${api.base-path}")
public class UsersInfoController {
    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("/me")
    public ResponseEntity<UsersInfoProjection> getUserInfo(@AuthenticationPrincipal UsersPrincipal principal){
        Long userId = principal.getUserId();

        UsersInfoProjection byUsersInfoProjection = userInfoService.findByUsersInfoProjection(userId);

        return ResponseEntity.status(HttpStatus.OK).body(byUsersInfoProjection);
    }
}
