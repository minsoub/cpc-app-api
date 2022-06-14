package com.bithumbsystems.cpc.api.v1.main.service;

import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import com.bithumbsystems.persistence.mongodb.main.model.entity.MainContents;
import com.bithumbsystems.persistence.mongodb.main.service.MainContentsDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainContentsService {

  private final MainContentsDomainService mainContentsDomainService;
  private final BoardDomainService boardDomainService;

  /**
   * 메인 컨텐츠 조회
   * @return
   */
  public Mono<MainContents> getMainContents() {
    return mainContentsDomainService.findOne();
  }

  /**
   * 가상 자산 동향 조회
   * @return
   */
  public Mono<List<BoardResponse>> getVirtualAssetTrends() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getVirtualAssetTrends)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 블록 체인 뉴스 조회
   * @return
   */
  public Mono<List<BoardResponse>> getBlockchainNews() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getBlockchainNews)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 투자 가이드 1 조회
   * @return
   */
  public Mono<List<BoardResponse>> getInvestmentGuide1() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getInvestmentGuide1)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 투자 가이드 2 조회
   * @return
   */
  public Mono<List<BoardResponse>> getInvestmentGuide2() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getInvestmentGuide2)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 투자 가이드 3 조회
   * @return
   */
  public Mono<List<BoardResponse>> getInvestmentGuide3() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getInvestmentGuide3)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }
}
