package com.productShop.repositories;

import com.productShop.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    Category findById(long id);

    @Query("SELECT c FROM Category c ORDER BY c.products.size DESC ")
    Set<Category> findAllByOrderByProductsCount();
}
