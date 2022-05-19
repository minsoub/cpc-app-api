package com.bithumbsystems.cpc.api.v1.board.controller;

import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.cpc.api.v1.board.service.BoardService;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Tag(name = "Board APIs", description = "게시판 관련 API")
public class BoardController {

  private final BoardService boardService;

  /**
   * 테스트용 초기 데이터 적재
   * @return
   */
  @GetMapping("/init1")
  public Mono<BoardMaster> initialize1() {

    // 게시판 마스터 데이터
    return boardService.saveBoardMaster(BoardMaster.builder()
        .id("sample-board-01")
        .siteId("cpc-prj")
        .name("샘플 게시판 01")
        .isUse(true)
        .type("10")
        .createDate(new Date())
        .createAdminAccountId("cpc-admin")
        .build());
  }

  /**
   * 테스트용 초기 데이터 적재
   * @return
   */
  @GetMapping("/init2")
  public Mono<Board> initialize2() {

    // 게시글 데이터
    return boardService.saveBoard(Board.builder()
        .id("1")
        .boardMasterId("sample-board-01")
        .no(1)
        .title("[함지현 기자의 EX레이더] 다른 거래소로 스테이블코인을 보낼 수 있을까?")
        .contents("국내 디지털자산 투자자 중 해외 거래소를 이용하는 데 진입장벽이 높다고 생각하는 투자자들이 상당수입니다. 미국 시민권자라서 코인베이스 계정을 바로 개설할 수 있는 게 아닌 이상 다음과 같은 절차를 거쳐야 하기 때문입니다.")
        .isUse(true)
        .createDate(new Date())
        .createAdminAccountId("cpc-admin")
        .build());
  }

  /**
   * 테스트용 초기 데이터 적재
   * @return
   */
  @GetMapping("/init3")
  public Mono<Board> initialize3() {

    // 게시글 데이터
    return boardService.saveBoard(Board.builder()
        .id("2")
        .boardMasterId("sample-board-01")
        .no(2)
        .title("[함지현 기자의 EX레이더] 거래소 ‘창조경제’의 추억")
        .contents("최근 경찰이 ‘퓨어빗’ 사기 사태로 인한 피해액 40억 원 중 30억 원을 피해자에게 돌려줬습니다. 정확히는 2018년 퓨어빗 일당이 가로챈 이더리움 1만 6907개 중 1060개를 되찾은 것인데요. 비록 회수율은 10%에 미치지 못했지만, 4년 동안 이더리움 가격이 16배 상승하면서 피해자들은 피해의 상당 부분을 회복할 수 있었습니다.")
        .isUse(true)
        .createDate(new Date())
        .createAdminAccountId("cpc-admin")
        .build());
  }

  /**
   * 게시판 마스터 정보 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  @GetMapping("/{boardId}/info")
  public Mono<BoardMaster> getBoardMasterInfo(@PathVariable String boardMasterId) {
    return boardService.getBoardMasterInfo(boardMasterId);
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  @GetMapping("/{boardMasterId}")
  public Flux<BoardResponse> getBoardDataList(@PathVariable String boardMasterId) {
    return boardService.getBoardDataList(boardMasterId);
  }

  /**
   * 게시글 조회
   * @param boardMasterId 게시판 ID
   * @param boardId 게시물 ID
   * @return
   */
  @GetMapping("/{boardMasterId}/{boardId}")
  public Mono<Board> getBoardData(@PathVariable String boardMasterId, @PathVariable String boardId) {
    return boardService.getBoardData(boardMasterId, boardId);
  }
}
