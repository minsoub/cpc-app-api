package com.bithumbsystems.cpc.api.v1.guide.service;

import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.v1.guide.exception.NewsException;
import com.bithumbsystems.cpc.api.v1.guide.mapper.NewsMapper;
import com.bithumbsystems.cpc.api.v1.guide.model.response.NewsResponse;
import com.bithumbsystems.persistence.mongodb.guide.model.entity.News;
import com.bithumbsystems.persistence.mongodb.guide.service.NewsDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

  private final NewsDomainService newsDomainService;

  /**
   * 블록체인 뉴스 목록 조회
   * @param keyword 키워드
   * @param pageRequest 페이지 정보
   * @return
   */
  public Mono<Page<NewsResponse>> getNewsList(String keyword, PageRequest pageRequest) {
    return newsDomainService.findPageBySearchText(keyword, pageRequest)
        .map(NewsMapper.INSTANCE::toDto)
        .collectList()
        .zipWith(newsDomainService.countBySearchText(keyword)
            .map(c -> c))
        .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
  }

  /**
   * 조회 수 증가
   * @param id 게시글 ID
   * @return
   */
  public Mono<News> incrementReadCount(Long id) {
    return newsDomainService.getNewsData(id)
        .switchIfEmpty(Mono.error(new NewsException(ErrorCode.NOT_FOUND_CONTENT)))
        .flatMap(news -> newsDomainService.incrementReadCount(id));
  }
}
