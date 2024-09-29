/**
 * The {@code BusinessDto$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.projections.business;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.records.BusinessType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessDto {
    private Long id;
    private String name;
    private String gstin;
    private Integer hsn;
    private Integer stateCode;
    private String businessLogo;
    private BusinessType businessType;
    private String invoicePrefix;
    private Integer invoiceSeq;

    public BusinessDto() {
    }

    public BusinessDto(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.gstin = business.getGstin();
        this.hsn = business.getHsn();
        this.stateCode = business.getStateCode();
        this.businessLogo = business.getBusinessLogo();
        this.businessType = business.getBusinessType();
        this.invoicePrefix = business.getInvoicePrefix();
        this.invoiceSeq = business.getInvoiceSeq();
    }
}
