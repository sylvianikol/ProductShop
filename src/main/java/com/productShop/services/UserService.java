package com.productShop.services;


import com.productShop.model.dtos.seed.UserSeedDto;
import com.productShop.model.dtos.view.UserWithSoldProductsViewDto;
import com.productShop.model.dtos.view.UsersCountDto;
import com.productShop.model.entities.User;

import java.util.List;

public interface UserService {

    void seedUsers(UserSeedDto[] dtos);

    User getRandomUser();

    List<UserWithSoldProductsViewDto> getUsersWithSoldProducts();

    UsersCountDto getUsersWithAtLeastOneSold();
}
