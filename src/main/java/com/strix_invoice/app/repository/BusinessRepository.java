package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    @Query("SELECT DISTINCT b FROM Business b LEFT JOIN FETCH b.address WHERE b.usersInfo.id = :userId")
    Set<Business> findByUserId(@Param("userId") Long userId);

}
