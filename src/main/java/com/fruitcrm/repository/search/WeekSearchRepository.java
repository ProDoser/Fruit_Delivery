package com.fruitcrm.repository.search;

import com.fruitcrm.domain.Week;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Week entity.
 */
public interface WeekSearchRepository extends ElasticsearchRepository<Week, Long> {
}
