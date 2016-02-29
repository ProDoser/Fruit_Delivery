package com.fruitcrm.repository;

import com.fruitcrm.domain.Week;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Week entity.
 */
public interface WeekRepository extends JpaRepository<Week,Long> {

}
