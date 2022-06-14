package com.bithumbsystems.cpc.api.v1.main.controller;

import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.main.model.response.MainContentsResponse;
import com.bithumbsystems.cpc.api.v1.main.service.MainContentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Tag(name = "Main APIs", description = "메인 화면 관련 API")
public class MainContentsController {

  private final MainContentsService mainContentsService;

  /**
   * 메인화면 선택된 컨텐츠 조회
   * @return
   */
  @GetMapping
  @Operation(description = "메인화면 선택된 컨텐츠 조회")
  public ResponseEntity<Mono<?>> getMainContents() {
    return ResponseEntity.ok().body(Mono.zip(mainContentsService.getMainContents(),
            mainContentsService.getVirtualAssetTrends(),
            mainContentsService.getBlockchainNews(),
            mainContentsService.getInvestmentGuide1(),
            mainContentsService.getInvestmentGuide2(),
            mainContentsService.getInvestmentGuide3())
        .flatMap(tuple -> Mono.just(MainContentsResponse.builder()
            .virtualAssetTrends(tuple.getT2())
            .blockchainNews(tuple.getT3())
            .investmentGuide1Id(tuple.getT1().getInvestmentGuide1Id())
            .investmentGuide1(tuple.getT4())
            .investmentGuide2Id(tuple.getT1().getInvestmentGuide2Id())
            .investmentGuide2(tuple.getT5())
            .investmentGuide3Id(tuple.getT1().getInvestmentGuide3Id())
            .investmentGuide3(tuple.getT6())
            .build()))
        .map(response -> new SingleResponse(response)));
  }
}
