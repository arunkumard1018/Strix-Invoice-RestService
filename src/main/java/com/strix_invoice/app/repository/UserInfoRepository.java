package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.projections.usersInfo.UsersInfoProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UsersInfo,Long> {

    @EntityGraph(attributePaths = {"businesses","activeBusiness"})
    @Query("SELECT u from UsersInfo u WHERE u.id = :userId")
    Optional<UsersInfoProjection> findByUsersInfoProjection(@Param("userId") Long userId);

}
