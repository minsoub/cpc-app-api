package com.bithumbsystems.persistence.mongodb.guide.repository;

import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NewsCustomRepository {

  Flux<News> findPageBySearchText(String keyword, LocalDate date, Pageable pageable);

  Mono<Long> countBySearchText(String keyword, LocalDate date);
}
