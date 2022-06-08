package com.bithumbsystems.persistence.mongodb.board.service;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardMasterRepository;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardDomainService {

  private final BoardMasterRepository boardMasterRepository;
  private final BoardRepository boardRepository;

  /**
   * 게시판 마스터 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Mono<BoardMaster> getBoardMasterInfo(String boardMasterId) {
    return boardMasterRepository.findById(boardMasterId);
  }

  /**
   * 게시판 마스터 저장 (테스트용)
   * @param boardMaster 게시판 마스터
   * @return
   */
  public Mono<BoardMaster> saveBoardMaster(BoardMaster boardMaster) {
    boardMaster.setIsUse(true);
    boardMaster.setCreateDate(LocalDateTime.now());
    return boardMasterRepository.save(boardMaster);
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @param keyword 키워드
   * @return
   */
  public Flux<Board> getBoards(String boardMasterId, String keyword) {
    Boolean isUse = true;
    return boardRepository.findByCondition(boardMasterId, isUse, keyword);
  }

  /**
   * 게시글 조회
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<Board> getBoardData(Long boardId) {
    return boardRepository.findById(boardId);
  }

  /**
   * 게시글 등록
   * @param board 게시글
   * @return
   */
  public Mono<Board> createBoard(Board board) {
    board.setIsUse(true);
    board.setCreateDate(LocalDateTime.now());
    return boardRepository.insert(board);
  }

  /**
   * 게시글 수정
   * @param board 게시글
   * @return
   */
  public Mono<Board> updateBoard(Board board) {
    board.setUpdateDate(LocalDateTime.now());
    return boardRepository.save(board);
  }

  /**
   * 게시글 삭제
   * @param board
   * @return
   */
  public Mono<Board> deleteBoard(Board board) {
    board.setIsUse(false);
    board.setUpdateDate(LocalDateTime.now());
    return boardRepository.save(board);
  }
}
