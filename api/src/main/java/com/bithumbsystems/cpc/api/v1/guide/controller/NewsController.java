package com.bithumbsystems.cpc.api.v1.guide.controller;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.DEFAULT_PAGE_SIZE;
import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.FIRST_PAGE_NUM;

import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.guide.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  /**
   * 블록체인 뉴스 목록 조회
   * @param query 검색어
   * @return
   */
  @GetMapping
  @Operation(summary = "블록체인 뉴스 목록 조회", description = "투자 가이드 > 블록체인 뉴스: 블록체인 뉴스 목록을 페이지 단위로 조회", tags = "투자 가이드 > 블록체인 뉴스")
  public ResponseEntity<Mono<?>> getNewsList(@RequestParam(name = "query", required = false, defaultValue = "") String query,
      @RequestParam(name = "page_no", defaultValue = FIRST_PAGE_NUM) int pageNo,
      @RequestParam(name = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
      throws UnsupportedEncodingException {
    String keyword = URLDecoder.decode(query, "UTF-8");
    log.info("keyword: {}", keyword.replaceAll("[\r\n]",""));
    return ResponseEntity.ok().body(newsService.getNewsList(keyword, PageRequest.of(pageNo, pageSize, Sort.by("posting_date").descending()))
        .map(SingleResponse::new));
  }

  /**
   * 블록체인 뉴스 조회 수 증가
   * @param id 게시글 ID
   * @return
   */
  @GetMapping(value = "/{id}")
  @Operation(summary = "블록체인 뉴스 조회 수 증가", description = "투자 가이드 > 블록체인 뉴스: 블록체인 뉴스 조회 수 증가", tags = "투자 가이드 > 블록체인 뉴스")
  public ResponseEntity<Mono<?>> incrementReadCount(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok().body(newsService.incrementReadCount(id)
        .then(Mono.just(new SingleResponse())));
  }
}
