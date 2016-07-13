package com.fiume.billingmechine.model;

/**
 * Created by Razi on 21-Jun-16.
 */
public class ProductReportModel {

    private String pId;
    private String pName;
    private String pSaledQty;
    private String pPrice;
    private String ptotalSaledPrice;

    public ProductReportModel() {
    }

    public ProductReportModel(String pId, String pName, String pSaledQty, String pPrice, String ptotalSaledPrice) {
        this.pId = pId;
        this.pName = pName;
        this.pSaledQty = pSaledQty;
        this.pPrice = pPrice;
        this.ptotalSaledPrice = ptotalSaledPrice;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpSaledQty() {
        return pSaledQty;
    }

    public void setpSaledQty(String pSaledQty) {
        this.pSaledQty = pSaledQty;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getPtotalSaledPrice() {
        return ptotalSaledPrice;
    }

    public void setPtotalSaledPrice(String ptotalSaledPrice) {
        this.ptotalSaledPrice = ptotalSaledPrice;
    }
}
