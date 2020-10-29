package com.productShop.repositories;

import com.productShop.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstNameAndLastNameAndAge(String firstName, String lastName, int age);

    User findById(long id);

    List<User> findAllByOrderByLastNameAscFirstNameAsc();

    @Query("SELECT u FROM User u WHERE u.sold.size > 0 ORDER BY u.sold.size DESC, u.lastName ")
    List<User> getAllUsingSoldNotEmpty();
}
