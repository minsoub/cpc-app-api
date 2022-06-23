package com.bithumbsystems.cpc.api.v1.board.controller;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.DEFAULT_PAGE_SIZE;
import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.FIRST_PAGE_NUM;

import com.bithumbsystems.cpc.api.core.model.response.MultiResponse;
import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
   * @param siteId 싸이트 ID
   * @return
   */
  @GetMapping("/{boardMasterId}/info")
  @Operation(description = "게시판 마스터 정보 조회")
  public ResponseEntity<Mono<?>> getBoardMasterInfo(@PathVariable String boardMasterId, @RequestHeader(value = "site_id") String siteId) {
    return ResponseEntity.ok().body(boardService.getBoardMasterInfo(boardMasterId, siteId)
        .map(SingleResponse::new)
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
  @Operation(description = "게시글 목록 조회")
  public ResponseEntity<Mono<?>> getBoards(
      @PathVariable String boardMasterId,
      @RequestParam(name = "query", required = false, defaultValue = "") String query,
      @RequestParam(name = "category_param", required = false) String categoryParam,
      @RequestParam(name = "page_no", defaultValue = FIRST_PAGE_NUM) int pageNo,
      @RequestParam(name = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize)
      throws UnsupportedEncodingException {
    String keyword = URLDecoder.decode(query, "UTF-8");
    log.info("keyword: {}", keyword);

    List<String> categories = new ArrayList<String>();
    if (StringUtils.isNotEmpty(categoryParam)) {
      categories = Arrays.asList(URLDecoder.decode(categoryParam, "UTF-8").split(";"));
    }
    log.debug("categories: {}", categories);

    return ResponseEntity.ok().body(boardService.getBoards(boardMasterId, keyword, categories, PageRequest.of(pageNo, pageSize, Sort.by("create_date").descending()))
        .map(SingleResponse::new));
  }

  /**
   * 공지 고정 게시글 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  @GetMapping("/{boardMasterId}/notice")
  @Operation(description = "공지 고정 게시글 조회")
  public ResponseEntity<Mono<?>> getNoticeBoards(@PathVariable String boardMasterId) {
    return ResponseEntity.ok().body(boardService.getNoticeBoards(boardMasterId)
        .collectList()
        .map(MultiResponse::new)
    );
  }

  /**
   * 게시글 조회
   * @param boardMasterId 게시판 ID
   * @param boardId 게시물 ID
   * @return
   */
  @GetMapping("/{boardMasterId}/{boardId}")
  @Operation(description = "게시글 조회")
  public ResponseEntity<Mono<?>> getBoardData(@PathVariable String boardMasterId, @PathVariable Long boardId) {
    return ResponseEntity.ok().body(boardService.getBoardData(boardId)
        .map(SingleResponse::new));
  }
}
