package com.bithumbsystems.cpc.api.v1.board.controller;

import com.bithumbsystems.cpc.api.core.model.response.MultiResponse;
import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.board.model.request.BoardRequest;
import com.bithumbsystems.cpc.api.v1.board.model.request.CommentRequest;
import com.bithumbsystems.cpc.api.v1.board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Tag(name = "Board APIs", description = "게시판 관련 API")
public class BoardController {

  private final BoardService boardService;

  /**
   * 게시판 마스터 정보 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  @GetMapping("/{boardMasterId}/info")
  public ResponseEntity<Mono<?>> getBoardMasterInfo(@PathVariable String boardMasterId) {
    return ResponseEntity.ok().body(
        boardService.getBoardMasterInfo(boardMasterId).map(res -> new SingleResponse(res))
    );
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  @GetMapping("/{boardMasterId}")
  @ResponseBody
  public ResponseEntity<Mono<?>> getBoardDataList(@PathVariable String boardMasterId) {
    return ResponseEntity.ok().body(boardService.getBoardDataList(boardMasterId)
        .collectList()
        .map(boardResponseList -> new MultiResponse(boardResponseList)));
  }

  /**
   * 게시글 조회
   * @param boardId 게시물 ID
   * @return
   */
  @GetMapping("/{boardMasterId}/{boardId}")
  public ResponseEntity<Mono<?>> getBoardData(@PathVariable Long boardId) {
    return ResponseEntity.ok().body(boardService.getBoardData(boardId)
        .map(boardResponse -> new SingleResponse(boardResponse)));
  }

  /**
   * 게시글 등록
   * @param boardMasterId 게시판 ID
   * @return
   */
  @PostMapping("/{boardMasterId}")
  public ResponseEntity<Mono<?>> createBoard(@PathVariable String boardMasterId, @RequestBody BoardRequest boardRequest) {
    boardRequest.setBoardMasterId(boardMasterId);
    return ResponseEntity.ok().body(boardService.createBoard(boardRequest)
        .map(boardResponse -> new SingleResponse(boardResponse)));
  }

  /**
   * 게시글 수정
   * @param boardRequest 게시글
   * @return
   */
  @PutMapping("/{boardMasterId}/{boardId}")
  public ResponseEntity<Mono<?>> updateBoard(@RequestBody BoardRequest boardRequest) {
    return ResponseEntity.ok().body(boardService.updateBoard(boardRequest)
        .map(boardResponse -> {
          log.info(boardResponse.toString());
          return new SingleResponse(boardResponse);
        }));
  }

  /**
   * 게시글 삭제
   * @param boardId 게시글
   * @return
   */
  @DeleteMapping("/{boardMasterId}/{boardId}")
  public ResponseEntity<Mono<?>> deleteBoard(@PathVariable Long boardId) {
    return ResponseEntity.ok().body(boardService.deleteBoard(boardId).then(
        Mono.just(new SingleResponse()))
    );
  }

  /**
   * 댓글 목록 조회
   * @param boardId 게시물 ID
   * @return
   */
  @GetMapping("/{boardId}/comments")
  public ResponseEntity<Mono<?>> getCommentList(@PathVariable Long boardId) {
    return ResponseEntity.ok().body(boardService.getCommentList(boardId)
        .collectList()
        .map(commentResponses -> new MultiResponse(commentResponses)));
  }

  /**
   * 댓글 조회
   * @param commentId 댓글 ID
   * @return
   */
  @GetMapping("/comment/{commentId}")
  public ResponseEntity<Mono<?>> getComment(@PathVariable String commentId) {
    return ResponseEntity.ok().body(boardService.getComment(commentId)
        .map(commentResponse -> new SingleResponse(commentResponse)));
  }

  /**
   * 댓글 등록
   * @param comment 댓글
   * @return
   */
  @PostMapping("/comment")
  public ResponseEntity<Mono<?>> createComment(@RequestBody CommentRequest comment) {
    return ResponseEntity.ok().body(boardService.createComment(comment)
        .map(commentResponse -> new SingleResponse(commentResponse)));
  }
}
