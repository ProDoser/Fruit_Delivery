package com.fruitcrm.repository;

import com.fruitcrm.domain.Orders;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Orders entity.
 */
public interface OrdersRepository extends JpaRepository<Orders,Long> {

}
