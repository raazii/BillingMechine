package com.fiume.billingmechine.model;

/**
 * Created by Razi on 12/16/2015.
 */
public class BillModel {

    public String id;
    public String name;
    public String arName;
    public String qty;
    public String unitPrice;
    public String price;


    public BillModel(String id, String name, String arName, String qty, String unitPrice, String price) {
        super();
        this.id = id;
        this.name = name;
        this.arName = arName;
        this.qty = qty;
        this.price = price;
        this.unitPrice = unitPrice;
    }

    public BillModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

}
