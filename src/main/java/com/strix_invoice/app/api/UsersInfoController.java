/**
 * The {@code UsersInfoController$} class represents Functionalities for UserDetais
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.api;

import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.projections.usersInfo.UsersInfoProjection;
import com.strix_invoice.app.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("${api.base-path}")
public class UsersInfoController {
    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("/me")
    public ResponseEntity<UsersInfoProjection> getUserInfo(@AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        UsersInfoProjection byUsersInfoProjection = userInfoService.findByUsersInfoProjection(userId);
        return ResponseEntity.status(HttpStatus.OK).body(byUsersInfoProjection);
    }

    @PatchMapping("/business/active/{businessId}")
    public ResponseEntity updateActiveBusiness(@PathVariable Long businessId,
                                               @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();

        userInfoService.updateActiveBusiness(businessId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("Message", "Success"));
    }

}
