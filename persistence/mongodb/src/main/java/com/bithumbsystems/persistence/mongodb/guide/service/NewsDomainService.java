package com.bithumbsystems.persistence.mongodb.guide.service;

import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import com.bithumbsystems.persistence.mongodb.guide.repository.NewsRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsDomainService {

  private final NewsRepository newsRepository;

  /**
   * 블록체인 뉴스 목록 조회
   * @param keyword 키워드
   * @return
   */
  public Flux<News> getNewsList(String keyword) {
    return newsRepository.findByCondition(keyword, LocalDate.now());
  }

  /**
   * 블록체인 뉴스 조회
   * @param id ID
   * @return
   */
  public Mono<News> getNewsData(Long id) {
    Boolean isUse = true;
    return newsRepository.findByIdAndIsUse(id, isUse);
  }
}
