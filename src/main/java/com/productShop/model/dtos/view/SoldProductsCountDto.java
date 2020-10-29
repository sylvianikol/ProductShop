package com.productShop.model.dtos.view;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class SoldProductsCountDto {

    @Expose
    private int count;
    @Expose
    private Set<SoldProductViewDto> products;

    public SoldProductsCountDto() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<SoldProductViewDto> getProducts() {
        return products;
    }

    public void setProducts(Set<SoldProductViewDto> products) {
        this.products = products;
    }
}
