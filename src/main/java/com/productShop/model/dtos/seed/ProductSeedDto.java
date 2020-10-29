package com.productShop.model.dtos.seed;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class ProductSeedDto {

    @Expose
    @Length(min = 3, message = "Product name's length must be minimum 3 characters long.")
    private String name;
    @Expose
    @DecimalMin(value = "0")
    private BigDecimal price;

    public ProductSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
