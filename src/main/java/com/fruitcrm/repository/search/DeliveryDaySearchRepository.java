package com.fruitcrm.repository.search;

import com.fruitcrm.domain.DeliveryDay;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DeliveryDay entity.
 */
public interface DeliveryDaySearchRepository extends ElasticsearchRepository<DeliveryDay, Long> {
}
