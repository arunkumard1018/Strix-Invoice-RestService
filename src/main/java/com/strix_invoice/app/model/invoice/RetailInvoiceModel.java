/**
 * The {@code RetailInvoiceModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model.invoice;

import com.strix_invoice.app.records.GSTType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RetailInvoiceModel {

    private GSTType gstType;
    private Double gstValue;
    private Double discount;
    private Double price;
    private String SKU;
    private String name;
    private Integer quantity;

}
