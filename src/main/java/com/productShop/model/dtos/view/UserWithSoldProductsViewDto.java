package com.productShop.model.dtos.view;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UserWithSoldProductsViewDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Set<SoldProductWithBuyerViewDto> soldProducts;

    public UserWithSoldProductsViewDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<SoldProductWithBuyerViewDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<SoldProductWithBuyerViewDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
