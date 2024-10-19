package com.PDFGenerator.Model;

import java.util.List;

public class PdfRequest {
    private String seller;
    private String sellerGstIn;
    private String sellerAddress;
    private String buyer;
    private String buyerGstIn;
    private String buyerAddress;
    private List<Item> items;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerGstIn() {
        return sellerGstIn;
    }

    public void setSellerGstIn(String sellerGstIn) {
        this.sellerGstIn = sellerGstIn;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyerGstIn() {
        return buyerGstIn;
    }

    public void setBuyerGstIn(String buyerGstIn) {
        this.buyerGstIn = buyerGstIn;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PdfRequest{" +
                "seller='" + seller + '\'' +
                ", sellerGstIn='" + sellerGstIn + '\'' +
                ", sellerAddress='" + sellerAddress + '\'' +
                ", buyer='" + buyer + '\'' +
                ", buyerGstIn='" + buyerGstIn + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", items=" + items +
                '}';
    }
}
