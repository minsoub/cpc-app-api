package com.bithumbsystems.cpc.api.v1.main.controller;

import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.main.model.response.MainContentsResponse;
import com.bithumbsystems.cpc.api.v1.main.service.MainContentsService;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainContentsController {

  private final MainContentsService mainContentsService;

  /**
   * 메인화면 하단 선택된 컨텐츠 조회
   * @return
   */
  @GetMapping(value = "/contents")
  @Operation(summary = "메인화면 선택된 컨텐츠 조회", description = "메인화면 > 하단 투자가이드 탭: 선택된 컨텐츠 조회", tags = "메인화면 > 하단 투자가이드 탭")
  public ResponseEntity<Mono<?>> getMainContents() {
    return ResponseEntity.ok().body(Mono.zip(mainContentsService.getDigitalAssetBasic(),
            mainContentsService.getInsightColumn(),
            mainContentsService.getDigitalAssetTrends(),
            mainContentsService.getBlockchainNews())
        .flatMap(tuple -> Mono.just(MainContentsResponse.builder()
            .digitalAssetBasic(tuple.getT1())
            .insightColumn(tuple.getT2())
            .digitalAssetTrends(tuple.getT3())
            .blockchainNews(tuple.getT4())
            .build()))
        .map(SingleResponse::new));
  }

  /**
   * 메인화면 상단 최근글 조회
   * @return
   */
  @GetMapping("/board/list")
  @Operation(summary = "메인화면 게시글 조회", description = "메인화면 > 상단 : 게시글 조회", tags = "메인화면 > 상단 게시글 조회")
  public ResponseEntity<Mono<?>> getMainBoardList(
      @RequestParam(name = "board_master_id") String boardMasterId,
      @RequestParam(name = "size", required = false, defaultValue = "2") Integer size) {
    return ResponseEntity.ok().body(
        mainContentsService.getBoardList(boardMasterId, size).map(SingleResponse::new));
  }

  /**
   * 메인화면 상단 빗썸 지수 조회
   */
//  @GetMapping("/btci")
//  public ResponseEntity<?> getBtci() {
//    return ResponseEntity.ok().body(
//        mainContentsService.getBtci().map(SingleResponse::new));
//  }


}
