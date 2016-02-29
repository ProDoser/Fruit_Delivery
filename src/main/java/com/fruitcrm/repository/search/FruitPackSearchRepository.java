package com.fruitcrm.repository.search;

import com.fruitcrm.domain.FruitPack;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FruitPack entity.
 */
public interface FruitPackSearchRepository extends ElasticsearchRepository<FruitPack, Long> {
}
