package com.productShop.services;

import com.productShop.model.dtos.seed.ProductSeedDto;
import com.productShop.model.dtos.view.ProductsInRangeViewDto;
import com.productShop.model.dtos.view.SoldProductViewDto;
import com.productShop.model.dtos.view.SoldProductWithBuyerViewDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductService {

    void seedProducts(ProductSeedDto[] productSeedDtos);

    List<ProductsInRangeViewDto> getAllProductsInRange(BigDecimal from, BigDecimal to);

    Set<SoldProductWithBuyerViewDto> getAllSoldProductsBySellerIdAndBuyerNotNull(Long id);

    BigDecimal getAveragePriceByCategoryId(Long id);

    BigDecimal getTotalRevenueByCategoryId(Long id);

    Set<SoldProductViewDto> getAllSoldProductsBySellerId(Long id);
}
