/**
 * The {@code Mapper$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.utility;

import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.model.BusinessModel;

public class Mapper {
    public static Business mapBusinessModelToBusiness(BusinessModel businessModel) {
        Business business = new Business();
        business.setName(businessModel.getName());
        business.setGstin(businessModel.getGstin());
        business.setStateCode(businessModel.getStateCode());
        business.setHsn(businessModel.getHsn());
        return business;
    }

    public static Address mapBusinessModelToAddress(BusinessModel businessModel) {
        Address address = new Address();
        address.setAddress(businessModel.getAddress());
        address.setCity(businessModel.getCity());
        address.setState(businessModel.getState());
        address.setZip(businessModel.getZip());
        return address;
    }
}
