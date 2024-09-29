package com.strix_invoice.app.projections.usersInfo;


import com.strix_invoice.app.projections.business.BusinessProjection;
import java.util.List;

public interface UsersInfoProjection {

    Long getId();
    String getName();
    BusinessProjection getActiveBusiness();
    List<BusinessProjection> getBusinesses();
}
