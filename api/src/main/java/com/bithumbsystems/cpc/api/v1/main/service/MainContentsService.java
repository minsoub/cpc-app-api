package com.bithumbsystems.cpc.api.v1.main.service;

import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.cpc.api.v1.guide.mapper.NewsMapper;
import com.bithumbsystems.cpc.api.v1.guide.model.response.NewsResponse;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import com.bithumbsystems.persistence.mongodb.guide.service.NewsDomainService;
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
  private final NewsDomainService newsDomainService;

  /**
   * 가상 자산 기초 조회
   * @return
   */
  public Mono<List<BoardResponse>> getDigitalAssetBasic() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getDigitalAssetBasic)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 인사이트 칼럼 조회
   * @return
   */
  public Mono<List<BoardResponse>> getInsightColumn() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getInsightColumn)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 가상 자산 동향 조회
   * @return
   */
  public Mono<List<BoardResponse>> getDigitalAssetTrends() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getDigitalAssetTrends)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 블록 체인 뉴스 조회
   * @return
   */
  public Mono<List<NewsResponse>> getBlockchainNews() {
    return mainContentsDomainService.findOne()
        .map(MainContents::getBlockchainNews)
        .flatMapMany(it -> Flux.fromIterable(it))
        .concatMap(boardId -> newsDomainService.getNewsData(boardId).map(NewsMapper.INSTANCE::toDto))
        .collectList();
  }

  /**
   * 상단 게시글 조회
   * @return
   */
  public Mono<List<BoardResponse>> getBoardList(String boardMasterId, int size) {
    return boardDomainService.getBoardSize(boardMasterId)
        .take(size)
        .map(BoardMapper.INSTANCE::toDto)
        .collectList();
  }
}
