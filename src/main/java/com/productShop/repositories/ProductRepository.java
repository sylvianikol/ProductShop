package com.productShop.repositories;

import com.productShop.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameAndPrice(String name, BigDecimal price);

    List<Product> findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal from, BigDecimal to);

    Set<Product> findAllBySellerIdAndBuyerIsNotNull(Long id);

    @Query(value = "SELECT avg(`p`.`price`) FROM `products` AS `p` " +
            "JOIN `products_categories` AS `pc` " +
            "ON `p`.`id` = `pc`.`products_id` " +
            "WHERE :id = `pc`.`categories_id` ", nativeQuery = true)
    BigDecimal getAveragePriceUsingCategoryId(@Param(value = "id") Long id);

    @Query(value = "SELECT sum(`p`.`price`) FROM `products` AS `p` " +
            "JOIN `products_categories` AS `pc` " +
            "ON `p`.`id` = `pc`.`products_id` " +
            "WHERE :id = `pc`.`categories_id` ", nativeQuery = true)
    BigDecimal getTotalRevenueUsingCategory(@Param(value = "id") Long id);

    Set<Product> findAllBySellerId(Long id);
}
