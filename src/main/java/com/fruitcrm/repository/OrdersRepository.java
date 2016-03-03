package com.fruitcrm.repository;

import com.fruitcrm.domain.DeliveryDay;
import com.fruitcrm.domain.Orders;

import com.fruitcrm.domain.Week;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


import java.util.List;

/**
 * Spring Data JPA repository for the Orders entity.
 */
public interface OrdersRepository extends JpaRepository<Orders,Long> {

    Page<Orders> findByDeliveryDay_weekdayAndWeek_weekNotLike(int deliveryDay , int week, Pageable pageable);

    //@Query("select o from Orders o where o.weekday = ?1")

    //List<Orders> findByDeliveryWeek(int weekday);

}
