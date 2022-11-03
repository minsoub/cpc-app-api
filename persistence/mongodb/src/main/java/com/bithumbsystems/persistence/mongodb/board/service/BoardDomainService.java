package com.bithumbsystems.persistence.mongodb.board.service;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardCustomRepository;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardMasterRepository;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardDomainService {

  private final BoardMasterRepository boardMasterRepository;
  private final BoardRepository boardRepository;
  private final BoardCustomRepository boardCustomRepository;

  /**
   * 게시판 마스터 조회
   * @param boardMasterId 게시판 ID
   * @param siteId 싸이트 ID
   * @return
   */
  public Mono<BoardMaster> getBoardMasterInfo(String boardMasterId, String siteId) {
    return boardMasterRepository.findByIdAndSiteId(boardMasterId, siteId);
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @param keyword 키워드
   * @param categories 카테고리
   * @param pageable 페이지 정보
   * @return
   */
  public Flux<Board> findPageBySearchText(String boardMasterId, String keyword, List<String> categories, Pageable pageable) {
    return boardCustomRepository.findPageBySearchText(boardMasterId, keyword, categories, pageable);
  }

  /**
   * 게시글 목록 건수 조회
   * @param boardMasterId 게시판 ID
   * @param keyword 키워드
   * @param categories 카테고리
   * @return
   */
  public Mono<Long> countBySearchText(String boardMasterId, String keyword, List<String> categories) {
    return boardCustomRepository.countBySearchText(boardMasterId, keyword, categories);
  }

  /**
   * 공지 고정 게시글 조회
   * @param boardMasterId
   * @return
   */
  public Flux<Board> getNoticeBoards(String boardMasterId) {
    return boardRepository.findALLByIsSetNotice(boardMasterId);
  }

  /**
   * 게시글 조회
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<Board> getBoardData(Long boardId) {
    return boardRepository.findBoardByIdAndIsUse(boardId, true);
  }

  /**
   * 조회 수 증가
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<Board> incrementReadCount(Long boardId) {
    return boardRepository.findById(boardId)
        .flatMap(board -> {
          board.setReadCount(board.getReadCount() + 1);
          return boardRepository.save(board);
        });
  }

  /**
   * 게시글 갯수 단위 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Flux<Board> getBoardSize(String boardMasterId) {
    return boardRepository.findBoardByBoardMasterIdAndIsUseOrderByCreateDateDesc(boardMasterId, true);
  }
  /**
   * 게시글 갯수 단위 조회
   * @return
   */
  public Flux<Board> getAllBoardList() {
    return boardRepository.findBoardByIsUseOrderByCreateDateDesc(true);
  }
}
