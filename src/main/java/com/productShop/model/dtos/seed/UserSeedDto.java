package com.productShop.model.dtos.seed;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UserSeedDto {

    @Expose
    private String firstName;

    @Expose
    @NotNull
    @Length(min = 3, message = "Last name's length must be minimum 3 characters long.")
    private String lastName;

    @Expose
    private int age;

    public UserSeedDto() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
