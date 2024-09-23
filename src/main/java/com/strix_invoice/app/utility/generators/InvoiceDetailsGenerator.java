/**
 * The {@code InvoiceDetailsGenerator$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.utility.generators;

import com.strix_invoice.app.Entity.invoice.TransportInvoiceDetails;
import com.strix_invoice.app.model.invoice.TransportInvoiceModel;

public class InvoiceDetailsGenerator {
    public static TransportInvoiceDetails generateTransportInvoiceDetails(TransportInvoiceModel model) {
        TransportInvoiceDetails transportInvoiceDetails = new TransportInvoiceDetails();

        transportInvoiceDetails.setDate(model.getDate());
        transportInvoiceDetails.setVehicleNo(model.getVehicleNo());
        transportInvoiceDetails.setSource(model.getSource());
        transportInvoiceDetails.setDestination(model.getDestination());
        transportInvoiceDetails.setGstType(model.getGstType());
        transportInvoiceDetails.setGstValue(model.getGstValue());
        transportInvoiceDetails.setDiscount(model.getDiscount());
        transportInvoiceDetails.setPrice(model.getPrice());

        return transportInvoiceDetails;
    }

    public static void updateTransportInvoiceDetails(TransportInvoiceDetails transportInvoiceDetails, TransportInvoiceModel model) {
        transportInvoiceDetails.setDate(model.getDate());
        transportInvoiceDetails.setVehicleNo(model.getVehicleNo());
        transportInvoiceDetails.setSource(model.getSource());
        transportInvoiceDetails.setDestination(model.getDestination());
        transportInvoiceDetails.setGstType(model.getGstType());
        transportInvoiceDetails.setGstValue(model.getGstValue());
        transportInvoiceDetails.setDiscount(model.getDiscount());
        transportInvoiceDetails.setPrice(model.getPrice());
    }
}
