package com.productShop.services;


import com.productShop.model.dtos.seed.CategorySeedDto;
import com.productShop.model.dtos.view.CategoryByProductViewDto;
import com.productShop.model.entities.Category;

import java.util.Set;

public interface CategoryService {

    void seedCategories(CategorySeedDto[] dtos);

    Set<Category> getRandomCategories();

    Set<CategoryByProductViewDto> getAllOrderedByProductsCount();
}
