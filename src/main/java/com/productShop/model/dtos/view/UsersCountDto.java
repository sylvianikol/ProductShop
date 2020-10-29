package com.productShop.model.dtos.view;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UsersCountDto {

    @Expose
    private int usersCount;
    @Expose
    List<UserViewDto> users;

    public UsersCountDto() {
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public List<UserViewDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserViewDto> users) {
        this.users = users;
    }
}
