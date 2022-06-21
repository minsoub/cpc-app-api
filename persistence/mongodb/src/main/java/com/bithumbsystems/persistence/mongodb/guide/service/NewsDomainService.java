package com.bithumbsystems.persistence.mongodb.guide.service;

import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import com.bithumbsystems.persistence.mongodb.guide.repository.NewsCustomRepository;
import com.bithumbsystems.persistence.mongodb.guide.repository.NewsRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsDomainService {

  private final NewsRepository newsRepository;
  private final NewsCustomRepository newsCustomRepository;

  /**
   * 블록체인 뉴스 목록 조회
   * @param keyword 키워드
   * @return
   */
  public Flux<News> findPageBySearchText(String keyword, Pageable pageable) {
    return newsCustomRepository.findPageBySearchText(keyword, LocalDate.now(), pageable);
  }

  /**
   * 블록체인 뉴스 목록 건수 조회
   * @param keyword 키워드
   * @return
   */
  public Mono<Long> countBySearchText(String keyword) {
    return newsCustomRepository.countBySearchText(keyword, LocalDate.now());
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

  /**
   * 조회 수 증가
   * @param id 게시글 ID
   * @return
   */
  public Mono<News> incrementReadCount(Long id) {
    return newsRepository.findById(id)
        .flatMap(news -> {
          news.setReadCount(news.getReadCount() + 1);
          return newsRepository.save(news);
        });
  }
}
