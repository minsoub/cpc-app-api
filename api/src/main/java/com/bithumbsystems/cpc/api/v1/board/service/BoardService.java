package com.bithumbsystems.cpc.api.v1.board.service;

import com.bithumbsystems.cpc.api.core.util.PageSupport;
import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMasterMapper;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardMasterResponse;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
   * @param page
   * @return
   */
  public Mono<PageSupport<BoardResponse>> getBoards(String boardMasterId, String keyword, List<String> categories, Pageable page) {
    return boardDomainService.getBoards(boardMasterId, keyword, categories)
        .collectList()
        .map(list -> new PageSupport<>(
            list
                .stream()
                .sorted(Comparator
                    .comparingLong(Board::getId)
                    .reversed())
                .skip((page.getPageNumber() - 1) * page.getPageSize())
                .limit(page.getPageSize())
                .map(BoardMapper.INSTANCE::toDto)
                .collect(Collectors.toList()),
            page.getPageNumber(), page.getPageSize(), list.size()));
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
