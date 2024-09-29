package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.projections.business.BusinessDataWithAddressProjection;
import com.strix_invoice.app.projections.business.BusinessInfoProjection;
import com.strix_invoice.app.projections.business.BusinessProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    //Query to find all Business Without address
    @Query("SELECT b FROM Business b WHERE b.usersInfo.id = :userId")
    Set<BusinessProjection> findBusinessProjectionByUserId(@Param("userId") Long userId);

    //Query  Fetch all businesses with their addresses
    @EntityGraph(attributePaths = {"address"})
    @Query("SELECT b FROM Business b LEFT JOIN b.address a")
    List<BusinessDataWithAddressProjection> findAllBusinessesWithAddress();

    @Query("SELECT b FROM Business b LEFT JOIN FETCH b.address a LEFT JOIN b.usersInfo u WHERE b.id = :businessId")
    Optional<BusinessDataWithAddressProjection> findBusinessWithAddressById(@Param("businessId") Long businessId);

    @Query("SELECT b FROM Business b WHERE b.id = :businessId")
    Optional<BusinessInfoProjection> findByBusinessProjection(Long businessId);

}
