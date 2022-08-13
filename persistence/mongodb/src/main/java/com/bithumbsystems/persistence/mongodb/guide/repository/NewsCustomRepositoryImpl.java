package com.bithumbsystems.persistence.mongodb.guide.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NewsCustomRepositoryImpl implements NewsCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<News> findPageBySearchText(String keyword, Pageable pageable) {
    return reactiveMongoTemplate.find(getQueryBySearchText(keyword).with(pageable), News.class);
  }

  @Override
  public Mono<Long> countBySearchText(String keyword) {
    return reactiveMongoTemplate.count(getQueryBySearchText(keyword), News.class);
  }

  private Query getQueryBySearchText(String keyword) {
    var query = new Query();

    query.addCriteria(
        new Criteria()
            .andOperator(
                where("is_use").is(true),
                where("title").regex(".*" + keyword.toLowerCase() + ".*", "i")
            )
    );
    query.with(Sort.by("posting_date").descending());
    return query;
  }
}
