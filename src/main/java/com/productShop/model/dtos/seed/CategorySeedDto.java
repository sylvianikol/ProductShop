package com.productShop.model.dtos.seed;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class CategorySeedDto {

    @Expose
    @Length(min = 3, max = 15, message = "Category name's length must be between 3 and 15 characters long.")
    private String name;

    public CategorySeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
