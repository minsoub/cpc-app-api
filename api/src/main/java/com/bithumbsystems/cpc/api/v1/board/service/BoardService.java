package com.bithumbsystems.cpc.api.v1.board.service;

import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMasterMapper;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardMasterResponse;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardDomainService boardDomainService;

  /**
   * 게시판 마스터 정보 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Mono<BoardMasterResponse> getBoardMasterInfo(String boardMasterId) {
    return boardDomainService.getBoardMasterInfo(boardMasterId).map(BoardMasterMapper.INSTANCE::toDto);
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @param keyword 키워드
   * @param categories 카테고리
   * @param pageRequest 페이지 정보
   * @return
   */
  public Mono<Page<BoardResponse>> getBoards(String boardMasterId, String keyword, List<String> categories, PageRequest pageRequest) {
    return boardDomainService.findPageBySearchText(boardMasterId, keyword, categories, pageRequest)
          .map(BoardMapper.INSTANCE::toDto)
          .collectList()
          .zipWith(boardDomainService.countBySearchText(boardMasterId, keyword, categories)
              .map(c -> c))
          .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
  }

  /**
   * 공지 고정 게시글 조회
   * @param boardMasterId
   * @return
   */
  public Flux<BoardResponse> getNoticeBoards(String boardMasterId) {
    return boardDomainService.getNoticeBoards(boardMasterId)
        .map(BoardMapper.INSTANCE::toDto);
  }

  /**
   * 게시글 조회
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<BoardResponse> getBoardData(Long boardId) {
    return boardDomainService.incrementReadCount(boardId)
        .flatMap(board -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto));
  }
}
