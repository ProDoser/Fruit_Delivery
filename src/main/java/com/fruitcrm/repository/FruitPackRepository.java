package com.fruitcrm.repository;

import com.fruitcrm.domain.FruitPack;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FruitPack entity.
 */
public interface FruitPackRepository extends JpaRepository<FruitPack,Long> {

}
