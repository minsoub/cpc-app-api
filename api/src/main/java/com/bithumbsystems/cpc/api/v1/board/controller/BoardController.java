package com.bithumbsystems.cpc.api.v1.board.controller;

import static com.bithumbsystems.cpc.api.core.util.PageSupport.DEFAULT_PAGE_SIZE;
import static com.bithumbsystems.cpc.api.core.util.PageSupport.FIRST_PAGE_NUM;

import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.board.model.enums.BoardType;
import com.bithumbsystems.cpc.api.v1.board.model.request.BoardRequest;
import com.bithumbsystems.cpc.api.v1.board.service.BoardService;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
   * 게시판 마스터 등록(테스트용)
   * @return
   */
  @PostMapping("/test")
  public ResponseEntity<Mono<?>> createBoardMaster() {
    return ResponseEntity.ok().body(boardService.saveBoardMaster(BoardMaster.builder()
                .id("sample-board-03")
                .siteId("cpc-prj")
                .type(BoardType.NOTICE.getCode())
                .name("샘플 게시판 03")
                .isUseTag(true)
//                .tags(Arrays.asList("가상화폐", "루나", "테라"))
                .isUse(true)
            .build())
        .map(boardResponse -> new SingleResponse(boardResponse)));
  }

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
   * @param query 검색어
   * @param pageNo - 페이지 번호
   * @param pageSize - 페이지 사이즈
   * @return
   */
  @GetMapping("/{boardMasterId}")
  @ResponseBody
  public ResponseEntity<Mono<?>> getBoards(
      @PathVariable String boardMasterId,
      @RequestParam(name = "query", required = false, defaultValue = "") String query,
      @RequestParam(name = "pageNo", defaultValue = FIRST_PAGE_NUM) int pageNo,
      @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
      throws UnsupportedEncodingException {
    String keyword = URLDecoder.decode(query, "UTF-8");
    log.info("keyword: {}", keyword);

    return ResponseEntity.ok().body(boardService.getBoards(boardMasterId, keyword, PageRequest.of(pageNo, pageSize))
        .map(c -> new SingleResponse(c)));
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
        .map(boardResponse -> new SingleResponse(boardResponse)));
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
}
