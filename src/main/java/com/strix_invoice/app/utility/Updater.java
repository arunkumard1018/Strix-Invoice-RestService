/**
 * The {@code Updater$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.utility;

import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.Customers;
import com.strix_invoice.app.model.AddressModel;
import com.strix_invoice.app.model.CustomersModel;

public class Updater {

    public static Customers updateCustomer(Customers customers, CustomersModel customersModel) {
        customers.setName(customersModel.getName());
        customers.setPhone(customersModel.getPhone());
        customers.setEmail(customersModel.getEmail());
        customers.setGstin(customersModel.getGstin());
        customers.setPan(customersModel.getPan());
        return customers;
    }

    public static Address updateAddress(Address address, AddressModel addressModel) {
        address.setAddress(addressModel.getAddress());
        address.setCity(addressModel.getCity());
        address.setState(addressModel.getState());
        address.setZip(addressModel.getZip());
        return address;
    }


}
