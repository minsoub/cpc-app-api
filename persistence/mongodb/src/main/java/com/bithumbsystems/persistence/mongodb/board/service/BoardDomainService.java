package com.bithumbsystems.persistence.mongodb.board.service;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardMasterRepository;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BoardDomainService {

  private final BoardMasterRepository boardMasterRepository;
  private final BoardRepository boardRepository;

  /**
   * 게시판 마스터 조회
   * @param boardId 게시판 ID
   * @return
   */
  public Mono<BoardMaster> getBoardMasterInfo(String boardId) {
    return boardMasterRepository.findById(boardId);
  }

  /**
   * 게시판 마스터 저장 (테스트용)
   * @param boardMaster 게시판 마스터
   * @return
   */
  public Mono<BoardMaster> saveBoardMaster(BoardMaster boardMaster) {
    return boardMasterRepository.save(boardMaster);
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Flux<Board> getBoardDataList(String boardMasterId) {
    return boardRepository.findByBoardMasterId(boardMasterId);
  }

  /**
   * 게시글 조회
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<Board> getBoardData(String boardMasterId, String boardId) {
    return boardRepository.findByIdAndBoardMasterId(boardId, boardMasterId);
  }

  /**
   * 게시글 저장 (테스트용)
   * @param board 게시글
   * @return
   */
  public Mono<Board> saveBoard(Board board) {
    return boardRepository.save(board);
  }
}
