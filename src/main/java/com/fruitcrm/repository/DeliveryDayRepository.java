package com.fruitcrm.repository;

import com.fruitcrm.domain.DeliveryDay;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DeliveryDay entity.
 */
public interface DeliveryDayRepository extends JpaRepository<DeliveryDay,Long> {

}
