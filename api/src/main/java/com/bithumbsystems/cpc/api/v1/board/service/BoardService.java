package com.bithumbsystems.cpc.api.v1.board.service;

import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardDomainService boardDomainService;

  /**
   * 게시판 마스터 정보 조회
   * @param boardId 게시판 ID
   * @return
   */
  public Mono<BoardMaster> getBoardMasterInfo(String boardId) {
    return boardDomainService.getBoardMasterInfo(boardId);
  }

  /**
   * 게시판 마스터 저장 (테스트용)
   * @param boardMaster 게시판 마스터
   * @return
   */
  public Mono<BoardMaster> saveBoardMaster(BoardMaster boardMaster) {
    return boardDomainService.saveBoardMaster(boardMaster);
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Flux<BoardResponse> getBoardDataList(String boardMasterId) {
    return boardDomainService.getBoardDataList(boardMasterId).map(BoardMapper.INSTANCE::toDto);
  }

  /**
   * 게시글 조회
   * @param boardId 게시판 ID
   * @return
   */
  public Mono<Board> getBoardData(String boardMasterId, String boardId) {
    return boardDomainService.getBoardData(boardMasterId, boardId);
  }

  /**
   * 게시글 저장 (테스트용)
   * @param board 게시글
   * @return
   */
  public Mono<Board> saveBoard(Board board) {
    return boardDomainService.saveBoard(board);
  }
}
