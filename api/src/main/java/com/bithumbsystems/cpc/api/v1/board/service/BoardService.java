package com.bithumbsystems.cpc.api.v1.board.service;

import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.v1.board.exception.BoardException;
import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.mapper.CommentMapper;
import com.bithumbsystems.cpc.api.v1.board.model.request.BoardRequest;
import com.bithumbsystems.cpc.api.v1.board.model.request.CommentRequest;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.cpc.api.v1.board.model.response.CommentResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Comment;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public Mono<BoardMaster> getBoardMasterInfo(String boardMasterId) {
    return boardDomainService.getBoardMasterInfo(boardMasterId);
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
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<BoardResponse> getBoardData(Long boardId) {
    return boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto);
  }

  /**
   * 게시글 등록
   * @param boardRequest 게시글
   * @return
   */
  public Mono<BoardResponse> createBoard(BoardRequest boardRequest) {
    return boardDomainService.createBoard(BoardMapper.INSTANCE.toEntity(boardRequest))
        .map(BoardMapper.INSTANCE::toDto)
        .switchIfEmpty(Mono.error(new BoardException(ErrorCode.FAIL_CREATE_CONTENT)));
  }

  /**
   * 게시글 수정
   *
   * @param boardRequest 게시글
   * @return
   */
  public Mono<Board> updateBoard(BoardRequest boardRequest) {
//    Long boardId = boardRequest.getId();
    return boardDomainService.getBoardData(boardRequest.getId())
        .flatMap(board -> {
          board.setTitle(boardRequest.getTitle());
          board.setContents(boardRequest.getContents());
          log.info(board.toString());
          return boardDomainService.updateBoard(board);
        }).doOnError((e) -> log.error(e.getMessage()))
        .switchIfEmpty(Mono.error(new BoardException(ErrorCode.FAIL_UPDATE_CONTENT)));
  }

  /**
   * 게시글 삭제
   * @param boardId 게시글
   * @return
   */
  public Mono<BoardResponse> deleteBoard(Long boardId) {
    return boardDomainService.getBoardData(boardId)
        .flatMap(boardDomainService::deleteBoard)
        .map(BoardMapper.INSTANCE::toDto)
        .switchIfEmpty(Mono.error(new BoardException(ErrorCode.FAIL_DELETE_CONTENT)));
  }

  /**
   * 댓글 목록 조회
   * @param boardId 게시글 ID
   * @return
   */
  public Flux<CommentResponse> getCommentList(Long boardId) {
    return boardDomainService.getCommentList(boardId).map(CommentMapper.INSTANCE::toDto);
  }

  /**
   * 댓글 조회
   * @param commentId
   * @return
   */
  public Mono<CommentResponse> getComment(String commentId) {
    return boardDomainService.getComment(commentId).map(CommentMapper.INSTANCE::toDto);
  }

  /**
   * 댓글 등록
   * @param comment 댓글
   * @return
   */
  public Mono<CommentResponse> createComment(CommentRequest comment) {
    return boardDomainService.createComment(Comment.builder()
        .boardId(comment.getBoardId())
        .contents(comment.getContents())
        .build()).map(CommentMapper.INSTANCE::toDto);
  }
}
