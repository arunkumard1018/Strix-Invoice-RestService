/**
 * The {@code TransportInvoiceModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model.invoice;


import com.strix_invoice.app.records.GSTType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TransportInvoiceModel {

    private Long id;
    private LocalDate date;
    private String vehicleNo;
    private String source;
    private String destination;
    private GSTType gstType;
    private Double gstValue;
    private Double discount;
    private Double price;

}
