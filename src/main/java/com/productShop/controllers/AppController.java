package com.productShop.controllers;

import com.google.gson.Gson;
import com.productShop.constants.GlobalConstants;
import com.productShop.model.dtos.seed.CategorySeedDto;
import com.productShop.model.dtos.seed.ProductSeedDto;
import com.productShop.model.dtos.seed.UserSeedDto;
import com.productShop.model.dtos.view.CategoryByProductViewDto;
import com.productShop.model.dtos.view.ProductsInRangeViewDto;
import com.productShop.model.dtos.view.UserWithSoldProductsViewDto;
import com.productShop.model.dtos.view.UsersCountDto;
import com.productShop.services.CategoryService;
import com.productShop.services.ProductService;
import com.productShop.services.UserService;
import com.productShop.utils.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class AppController implements CommandLineRunner {

    private final Gson gson;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final FileIOUtil fileIOUtil;
    private final BufferedReader reader;

    @Autowired
    public AppController(Gson gson,
                         CategoryService categoryService,
                         UserService userService,
                         ProductService productService,
                         FileIOUtil fileIOUtil,
                         BufferedReader reader) {
        this.gson = gson;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.fileIOUtil = fileIOUtil;
        this.reader = reader;
    }

    @Override
    public void run(String... args) throws Exception {

        // 2. Seed Database
        this.seedCategories();
        this.seedUsers();
        this.seedProducts();

        // 3. Query and Export Data

        // Query 1 – Products in Range
//        this.writeProductsInRange();

        // Query 2 – Successfully Sold Products
//        this.writeSoldProducts();

        // Query 3 – Categories by Products Count
//        this.writeCategoriesByProductsCount();

        // Query 4 – Users and Products
        this.writeUsersAndProducts();
    }


    // 3. Query and Export Data

    // Query 4 – Users and Products
    private void writeUsersAndProducts() throws IOException {
        UsersCountDto dto =
                this.userService.getUsersWithAtLeastOneSold();

        String dtoToJson = this.gson.toJson(dto);

        this.fileIOUtil.write(dtoToJson, GlobalConstants.USERS_AND_PRODUCTS_FILE_PATH);
        System.out.println("Users with at least 1 Product Sold written to 'files/outputUsersAndProducts'");
    }

    // Query 3 – Categories by Products Count
    private void writeCategoriesByProductsCount() throws IOException {
        Set<CategoryByProductViewDto> dtos
                = this.categoryService.getAllOrderedByProductsCount();

        String dtosToJson = this.gson.toJson(dtos);

        this.fileIOUtil.write(dtosToJson, GlobalConstants.CATEGORIES_BY_PRODUCTS_COUNT_FILE_PATH);
        System.out.println("Categories sorted by Products Count written to 'files/outputUsersWithSoldProducts'");
    }

    // Query 2 – Successfully Sold Products
    private void writeSoldProducts() throws IOException {
        List<UserWithSoldProductsViewDto> dtos =
                this.userService.getUsersWithSoldProducts();

        String dtosToJson = this.gson.toJson(dtos);

        this.fileIOUtil.write(dtosToJson, GlobalConstants.USERS_WITH_SOLD_PRODUCTS_FILE_PATH);
        System.out.println("Users with Sold Products written to 'files/outputUsersWithSoldProducts'");
    }

    // Query 1 – Products in Range
    private void writeProductsInRange() throws IOException {
        System.out.println("Enter min price: ");
        BigDecimal from = new BigDecimal(this.reader.readLine());
        System.out.println("Enter max price: ");
        BigDecimal to = new BigDecimal(this.reader.readLine());

        List<ProductsInRangeViewDto> dtos =
                this.productService.getAllProductsInRange(from, to);

        String dtosToJson = this.gson.toJson(dtos);

        this.fileIOUtil.write(dtosToJson, GlobalConstants.PRODUCTS_IN_RANGE_FILE_PATH);

        System.out.println("Products in Range written to `files/outputProductsInRange`");
    }

    // 2. Seed Database

    private void seedProducts() throws FileNotFoundException {
        ProductSeedDto[] productSeedDtos = this.gson
                .fromJson(new FileReader(GlobalConstants.PRODUCTS_FILE_PATH), ProductSeedDto[].class);

        this.productService.seedProducts(productSeedDtos);
    }

    private void seedUsers() throws FileNotFoundException {

        UserSeedDto[] userSeedDtos = this.gson
                .fromJson(new FileReader(GlobalConstants.USERS_FILE_PATH), UserSeedDto[].class);

        this.userService.seedUsers(userSeedDtos);
    }

    private void seedCategories() throws FileNotFoundException {

        CategorySeedDto[] dtos = this.gson
                .fromJson(new FileReader(GlobalConstants.CATEGORIES_FILE_PATH), CategorySeedDto[].class);

        this.categoryService.seedCategories(dtos);
    }
}
