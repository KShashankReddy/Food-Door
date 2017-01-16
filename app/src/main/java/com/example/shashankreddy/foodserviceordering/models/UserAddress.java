package com.example.shashankreddy.foodserviceordering.models;

public class UserAddress {
    String Address1,Address2,country,postalCode;
    Boolean currentAddress=true;

    public UserAddress(String address1, String address2, String country, String postalCode) {
        Address1 = address1;
        Address2 = address2;
        this.country = country;
        this.postalCode = postalCode;
    }

    public UserAddress() {

    }

    public String getAddress1() {
        return Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Boolean currentAddress) {
        this.currentAddress = currentAddress;
    }

}
