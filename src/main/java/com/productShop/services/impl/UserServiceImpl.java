package com.productShop.services.impl;

import com.productShop.model.dtos.seed.UserSeedDto;
import com.productShop.model.dtos.view.*;
import com.productShop.model.entities.User;
import com.productShop.repositories.UserRepository;
import com.productShop.services.ProductService;
import com.productShop.services.UserService;
import com.productShop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           @Lazy ProductService productService,
                           ModelMapper modelMapper,
                           ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers(UserSeedDto[] userSeedDtos) {
        for (UserSeedDto dto : userSeedDtos) {
            if (this.userRepository.findByFirstNameAndLastNameAndAge(
                    dto.getFirstName(), dto.getLastName(), dto.getAge()) != null) {
                continue;
            }

            if (this.validationUtil.isValid(dto)) {
                User user = this.modelMapper.map(dto, User.class);
                this.userRepository.saveAndFlush(user);
            } else {
                this.validationUtil.violations(dto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }
    }

    @Override
    public User getRandomUser() {
        Random random = new Random();
        long userId = random.nextInt((int) this.userRepository.count()) + 1;
        return this.userRepository.findById(userId);
    }

    @Override
    public List<UserWithSoldProductsViewDto> getUsersWithSoldProducts() {

        List<UserWithSoldProductsViewDto> resultUserDtos = new ArrayList<>();

        List<User> users = this.userRepository.findAllByOrderByLastNameAscFirstNameAsc();

        for (User user : users) {
            Set<SoldProductWithBuyerViewDto> soldProductWithBuyerViewDtos =
                    this.productService.getAllSoldProductsBySellerIdAndBuyerNotNull(user.getId());


            if (soldProductWithBuyerViewDtos.isEmpty()) continue;

            UserWithSoldProductsViewDto userViewDto =
                    this.modelMapper.map(user, UserWithSoldProductsViewDto.class);

            userViewDto.setSoldProducts(soldProductWithBuyerViewDtos);

            resultUserDtos.add(userViewDto);
        }

        return resultUserDtos;
    }

    @Override
    public UsersCountDto getUsersWithAtLeastOneSold() {

        List<User> users = this.userRepository.getAllUsingSoldNotEmpty();
        List<UserViewDto> userViewDtos = new ArrayList<>();

        for (User user : users) {

            UserViewDto userViewDto = this.modelMapper
                    .map(user, UserViewDto.class);

            Set<SoldProductViewDto> soldProductViewDtos =
                    this.productService.getAllSoldProductsBySellerId(user.getId());

            SoldProductsCountDto soldProductsCountDto = new SoldProductsCountDto();
            soldProductsCountDto.setCount(soldProductViewDtos.size());
            soldProductsCountDto.setProducts(soldProductViewDtos);

            userViewDto.setSoldProducts(soldProductsCountDto);

            userViewDtos.add(userViewDto);
        }

        UsersCountDto userCountDto = new UsersCountDto();
        userCountDto.setUsersCount(users.size());
        userCountDto.setUsers(userViewDtos);

        return userCountDto;
    }
}
