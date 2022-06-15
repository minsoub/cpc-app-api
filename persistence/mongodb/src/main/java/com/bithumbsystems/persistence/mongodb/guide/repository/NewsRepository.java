package com.bithumbsystems.persistence.mongodb.guide.repository;

import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import java.time.LocalDate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NewsRepository extends ReactiveMongoRepository<News, Long> {
  @Query("{$and : [{title: {$regex: ?0, $options: 'i'}}, {postingDate: {$lt:  ?1}}, {isUse: true}]}")
  Flux<News> findByCondition(String keyword, LocalDate date);

  Mono<News> findByIdAndIsUse(Long id, Boolean isUse);
}
