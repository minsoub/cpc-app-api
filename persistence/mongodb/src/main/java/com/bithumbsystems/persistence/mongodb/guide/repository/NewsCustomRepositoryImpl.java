package com.bithumbsystems.persistence.mongodb.guide.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
  public Flux<News> findPageBySearchText(String keyword, LocalDate date, Pageable pageable) {
    return reactiveMongoTemplate.find(getQueryBySearchText(keyword, date).with(pageable), News.class);
  }

  @Override
  public Mono<Long> countBySearchText(String keyword, LocalDate date) {
    return reactiveMongoTemplate.count(getQueryBySearchText(keyword, date), News.class);
  }

  private Query getQueryBySearchText(String keyword, LocalDate date) {
    var query = new Query();

    query.addCriteria(
        new Criteria()
            .andOperator(
                where("postingDate").lt(date),
                where("is_use").is(true),
                where("title").regex(".*" + keyword.toLowerCase() + ".*", "i")
            )
    );
    return query;
  }
}
