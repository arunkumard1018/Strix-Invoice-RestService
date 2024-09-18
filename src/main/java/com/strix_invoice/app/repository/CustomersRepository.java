package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.Customers;
import com.strix_invoice.app.projections.customers.CustomersProjection;
import com.strix_invoice.app.projections.customers.CustomersWithAddressProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomersRepository extends JpaRepository<Customers, Long> {

    /**
     * @param userId to Fetch All the Customers belongs to specific user
     * @return List of {@link CustomersProjection} for the userId
     */
    @Query("SELECT c FROM Customers c WHERE c.usersInfo.id = :userId")
    List<CustomersProjection> findAllCustomersByProjection(@Param("userId") Long userId);


    @Query("SELECT c FROM Customers c WHERE c.business.id= :businessId")
    List<CustomersProjection> findAllCustomersByProjectionForBusiness(@Param("businessId") Long businessId);

    /**
     * Retrieves all the Customers present in the database using the CustomersWithAddressProjection
     *
     * @return A list of {@link CustomersWithAddressProjection} containing customer details
     * along with their billing and shipping addresses.
     * @see CustomersWithAddressProjection
     */
    @EntityGraph(attributePaths = {"shippingAddress", "billingAddress"})
    @Query("SELECT c FROM Customers c")
    List<CustomersWithAddressProjection> retrieveAllCustomers();

    /**
     * @param customerId to Fetch the Specific customer with Projection.
     * @return {@link CustomersWithAddressProjection} with Shipping Address and Billing Address
     * @see CustomersWithAddressProjection
     */
    @Query("SELECT c FROM Customers c " +
            "LEFT JOIN FETCH c.billingAddress b" +
            "LEFT JOIN FETCH c.shippingAddress s" +
            "LEFT JOIN FETCH c.usersInfo u WHERE c.id = :customerId")
    Optional<CustomersWithAddressProjection> retrieveCustomersWithAddressProjection(@Param("customerId") Long customerId);
}


// it will Fetch all the details even the details not present in Projection
//@EntityGraph(attributePaths = {"billingAddress", "shippingAddress", "usersInfo"})
//@Query("SELECT c FROM Customers c WHERE c.id = :customerId")
