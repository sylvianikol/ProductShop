package com.productShop.services.impl;

import com.productShop.model.dtos.seed.CategorySeedDto;
import com.productShop.model.dtos.view.CategoryByProductViewDto;
import com.productShop.model.entities.Category;
import com.productShop.repositories.CategoryRepository;
import com.productShop.services.CategoryService;
import com.productShop.services.ProductService;
import com.productShop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               @Lazy ProductService productService,
                               ModelMapper modelMapper,
                               ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories(CategorySeedDto[] dtos) {
        for (CategorySeedDto dto : dtos) {
            if (this.categoryRepository.findByName(dto.getName()) != null) {
                continue;
            }

            if (this.validationUtil.isValid(dto)) {
                Category category = this.modelMapper.map(dto, Category.class);

                this.categoryRepository.saveAndFlush(category);
            } else {
                this.validationUtil.violations(dto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }
    }

    @Override
    public Set<Category> getRandomCategories() {
        Random random = new Random();
        int randomCounter = random.nextInt(3) + 1;

        Set<Category> categories = new HashSet<>();

        for (int i = 0; i < randomCounter; i++) {
            long randomId = random.nextInt((int) this.categoryRepository.count()) + 1;

            categories.add(this.categoryRepository.findById(randomId));
        }
        return categories;
    }

    @Override
    public Set<CategoryByProductViewDto> getAllOrderedByProductsCount() {
        Set<Category> categories = this.categoryRepository.findAllByOrderByProductsCount();
        Set<CategoryByProductViewDto> categoryViewDtos = new LinkedHashSet<>();

        for (Category category : categories) {
            CategoryByProductViewDto categoryViewDto =
                    this.modelMapper.map(category, CategoryByProductViewDto.class);

            categoryViewDto.setCategory(category.getName());
            categoryViewDto.setProductsCount(category.getProducts().size());
            categoryViewDto.setAveragePrice(this.productService.getAveragePriceByCategoryId(category.getId()));
            categoryViewDto.setTotalRevenue(this.productService.getTotalRevenueByCategoryId(category.getId()));

            categoryViewDtos.add(categoryViewDto);
        }

        return categoryViewDtos;
    }
}
