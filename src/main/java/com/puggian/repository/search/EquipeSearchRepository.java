package com.puggian.repository.search;

import com.puggian.domain.Equipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Equipe entity.
 */
public interface EquipeSearchRepository extends ElasticsearchRepository<Equipe, Long> {
}
