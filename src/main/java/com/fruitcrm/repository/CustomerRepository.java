package com.fruitcrm.repository;

import com.fruitcrm.domain.Customer;
import com.fruitcrm.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface
CustomerRepository extends JpaRepository<Customer,Long> {


}
