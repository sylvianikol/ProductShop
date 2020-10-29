package com.productShop.services.impl;

import com.productShop.model.dtos.seed.ProductSeedDto;
import com.productShop.model.dtos.view.ProductsInRangeViewDto;
import com.productShop.model.dtos.view.SoldProductViewDto;
import com.productShop.model.dtos.view.SoldProductWithBuyerViewDto;
import com.productShop.model.entities.Product;
import com.productShop.repositories.ProductRepository;
import com.productShop.services.CategoryService;
import com.productShop.services.ProductService;
import com.productShop.services.UserService;
import com.productShop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              @Lazy UserService userService,
                              CategoryService categoryService,
                              ModelMapper modelMapper,
                              ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public void seedProducts(ProductSeedDto[] productSeedDtos) {

        for (ProductSeedDto dto : productSeedDtos) {
            if (this.productRepository.findByNameAndPrice(dto.getName(), dto.getPrice()) != null) {
                continue;
            }

            if (this.validationUtil.isValid(dto)) {
                Product product = this.modelMapper.map(dto, Product.class);

                product.setSeller(this.userService.getRandomUser());
                this.setRandomBuyer(product);
                product.setCategories(this.categoryService.getRandomCategories());

                this.productRepository.saveAndFlush(product);
            } else {
                this.validationUtil.violations(dto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        }
    }

    @Override
    public List<ProductsInRangeViewDto> getAllProductsInRange(BigDecimal from, BigDecimal to) {
        return this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPrice(from, to)
                .stream()
                .map(p -> {
                    ProductsInRangeViewDto productsInRangeViewDto = this.modelMapper.map(p, ProductsInRangeViewDto.class);

                    productsInRangeViewDto.setSeller(String.format("%s %s",
                            p.getSeller().getFirstName(),
                            p.getSeller().getLastName()));

                    return productsInRangeViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Set<SoldProductWithBuyerViewDto> getAllSoldProductsBySellerIdAndBuyerNotNull(Long id) {

        Set<Product> soldProducts =
                this.productRepository.findAllBySellerIdAndBuyerIsNotNull(id);

        return soldProducts.stream()
                .map(product -> {
                    SoldProductWithBuyerViewDto productViewDto =
                            this.modelMapper.map(product, SoldProductWithBuyerViewDto.class);

                    productViewDto.setBuyerFirstName(product.getBuyer().getFirstName());
                    productViewDto.setBuyerLastName(product.getBuyer().getLastName());

                    return productViewDto;

                })
                .collect(Collectors.toSet());
    }

    @Override
    public BigDecimal getAveragePriceByCategoryId(Long id) {
        return this.productRepository.getAveragePriceUsingCategoryId(id);
    }

    @Override
    public BigDecimal getTotalRevenueByCategoryId(Long id) {
        return this.productRepository.getTotalRevenueUsingCategory(id);
    }

    @Override
    public Set<SoldProductViewDto> getAllSoldProductsBySellerId(Long id) {

        Set<Product> products = this.productRepository.findAllBySellerId(id);

        Set<SoldProductViewDto> soldProductViewDtos = new HashSet<>();

        for (Product product : products) {
            SoldProductViewDto soldProductViewDto =
                    this.modelMapper.map(product, SoldProductViewDto.class);

            soldProductViewDtos.add(soldProductViewDto);
        }

        return soldProductViewDtos;
    }


    private void setRandomBuyer(Product product) {
        Random random = new Random();
        int id = random.nextInt(2);

        if (id == 1) {
            product.setBuyer(this.userService.getRandomUser());
        }
    }
}
