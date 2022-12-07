package com.bithumbsystems.cpc.api.v1.board.service;

import com.bithumbsystems.cpc.api.core.exception.InvalidParameterException;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import com.bithumbsystems.cpc.api.core.util.SearchTextUtil;
import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMapper;
import com.bithumbsystems.cpc.api.v1.board.mapper.BoardMasterMapper;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardMasterResponse;
import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
   * @param siteId 싸이트 ID
   * @return
   */
  public Mono<BoardMasterResponse> getBoardMasterInfo(String boardMasterId, String siteId) {
    return boardDomainService.getBoardMasterInfo(boardMasterId, siteId).map(BoardMasterMapper.INSTANCE::toDto)
        .switchIfEmpty(Mono.error(new InvalidParameterException(ErrorCode.NOT_FOUND_CONTENT)));
  }

  /**
   * 게시글 목록 조회
   * @param boardMasterId 게시판 ID
   * @param searchCategory 검색 카테고리
   * @param keyword 키워드
   * @param categories 카테고리
   * @param pageRequest 페이지 정보
   * @return
   */
  public Mono<Page<BoardResponse>> getBoards(String boardMasterId, String searchCategory, String keyword, List<String> categories, PageRequest pageRequest) {

    String searchText = SearchTextUtil.specialCharacterValidation(keyword);

    return boardDomainService.findPageBySearchText(boardMasterId, searchCategory, searchText, categories, pageRequest)
          .map(BoardMapper.INSTANCE::toDto)
          .collectList()
          .zipWith(boardDomainService.countBySearchText(boardMasterId, searchCategory, searchText, categories)
              .map(c -> c))
          .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
  }

  /**
   * 공지 고정 게시글 조회
   * @param boardMasterId
   * @return
   */
  public Flux<BoardResponse> getNoticeBoards(String boardMasterId) {
    return boardDomainService.getNoticeBoards(boardMasterId)
        .map(BoardMapper.INSTANCE::toDto);
  }

  /**
   * 게시글 조회
   * @param boardId 게시글 ID
   * @return
   */
  public Mono<BoardResponse> getBoardData(Long boardId) {
    return boardDomainService.incrementReadCount(boardId)
        .flatMap(board -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
        .switchIfEmpty(Mono.error(new InvalidParameterException(ErrorCode.NOT_FOUND_CONTENT)));
  }

  /**
   * 게시글 상세 조회 및 이전/다음 글 조회
   *
   * @param boardMasterId
   * @param boardId
   * @param searchCategory
   * @param keyword
   * @param categories
   * @return
   */
  public Mono<BoardResponse> getBoardData(String boardMasterId, Long boardId, String searchCategory,  String keyword, List<String> categories) {
    return boardDomainService.incrementReadCount(boardId)
            .flatMap(board -> boardDomainService.getBoardData(boardId).map(BoardMapper.INSTANCE::toDto))
            .flatMap(boardData -> {
                return boardDomainService.findPageBySearchPrevData(boardMasterId, boardId, searchCategory, keyword, categories)
                        .flatMap(r1 -> {
                          boardData.setPrevId(r1.getId());
                          boardData.setPrevTitle(r1.getTitle());
                          boardData.setPrevCreateDate(r1.getCreateDate());
                          return Mono.just(boardData);
                        })
                        .switchIfEmpty(Mono.just(boardData));
            })
            .flatMap(boardData -> {
              return boardDomainService.findPageBySearchNextData(boardMasterId, boardId, searchCategory, keyword, categories)
                      .flatMap(r2 -> {
                        boardData.setNextId(r2.getId());
                        boardData.setNextTitle(r2.getTitle());
                        boardData.setNextCreateDate(r2.getCreateDate());
                        return Mono.just(boardData);
                      })
                      .switchIfEmpty(Mono.just(boardData));
            })
            .switchIfEmpty(Mono.error(new InvalidParameterException(ErrorCode.NOT_FOUND_CONTENT)));
  }
}
