package com.bithumbsystems.persistence.mongodb.board.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<Board> findPageBySearchText(String boardMasterId, String searchCategory, String keyword, List<String> categories, Pageable pageable) {
    return reactiveMongoTemplate.find(getQueryBySearchText(boardMasterId, searchCategory, keyword, categories).with(pageable), Board.class);
  }


  @Override
  public Mono<Board> findPageBySearchPrevData(String boardMasterId, Long boardId, String searchCategory, String keyword, List<String> categories) {
    return reactiveMongoTemplate.findOne(getQueryBySearchPrevText(boardMasterId, boardId, searchCategory, keyword, categories), Board.class);
  }

  @Override
  public Mono<Board> findPageBySearchNextData(String boardMasterId, Long boardId, String searchCategory, String keyword, List<String> categories) {
    return reactiveMongoTemplate.findOne(getQueryBySearchNextText(boardMasterId, boardId, searchCategory, keyword, categories), Board.class);
  }


  @Override
  public Mono<Long> countBySearchText(String boardMasterId, String searchCategory, String keyword, List<String> categories) {
    return reactiveMongoTemplate.count(getQueryBySearchText(boardMasterId, searchCategory, keyword, categories), Board.class);
  }

  private Query getQueryBySearchText(String boardMasterId, String searchCategory, String keyword, List<String> categories) {
    var query = new Query();
    Criteria criteria = new Criteria();

    if (categories.size() > 0) {
      criteria.andOperator(
              where("board_master_id").is(boardMasterId),
              where("is_use").is(true),
              where("is_set_notice").is(false),
              where("category").in(categories)
      );
    } else {
      criteria.andOperator(
              where("board_master_id").is(boardMasterId),
              where("is_use").is(true),
              where("is_set_notice").is(false)
      );
    }
    if (StringUtils.hasLength(keyword)) {
        if (searchCategory.equals("1")) {  // 제목 + 본문
          criteria.orOperator(
                  where("title").regex(".*" + keyword.toLowerCase() + ".*", "i"),
                  where("contents").regex(".*" + keyword.toLowerCase() + ".*", "i")
          );
        } else if(searchCategory.equals("2")) { // 제목
          criteria.orOperator(
                  where("title").regex(".*" + keyword.toLowerCase() + ".*", "i")
          );
        } else if(searchCategory.equals("3")){  // 해시태그
          criteria.orOperator(
                  where("tags").regex(".*" + keyword.toLowerCase() + ".*", "i")
          );
        }
    }


    query.addCriteria(criteria);

    return query;
  }

  private Query getQueryBySearchPrevText(String boardMasterId, Long boardId, String searchCategory, String keyword, List<String> categories) {
    var query = new Query();
    Criteria criteria = new Criteria();

    if (categories.size() > 0) {
      criteria.andOperator(
              where("board_master_id").is(boardMasterId),
              where("id").lt(boardId),
              where("is_use").is(true),
              where("is_set_notice").is(false),
              where("category").in(categories)
      );
    } else {
      criteria.andOperator(
              where("board_master_id").is(boardMasterId),
              where("id").lt(boardId),
              where("is_use").is(true),
              where("is_set_notice").is(false)
      );
    }
    if (StringUtils.hasLength(keyword)) {
      if (searchCategory.equals("1")) {  // 제목 + 본문
        criteria.orOperator(
                where("title").regex(".*" + keyword.toLowerCase() + ".*", "i"),
                where("contents").regex(".*" + keyword.toLowerCase() + ".*", "i")
        );
      } else if(searchCategory.equals("2")) { // 제목
        criteria.orOperator(
                where("title").regex(".*" + keyword.toLowerCase() + ".*", "i")
        );
      } else if(searchCategory.equals("3")){  // 해시태그
        criteria.orOperator(
                where("tags").regex(".*" + keyword.toLowerCase() + ".*", "i")
        );
      }
    }
    query.addCriteria(criteria);
    query.with(Sort.by("create_date").descending());
    query.limit(1);

    return query;
  }

  private Query getQueryBySearchNextText(String boardMasterId, Long boardId, String searchCategory, String keyword, List<String> categories) {
    var query = new Query();
    Criteria criteria = new Criteria();

    if (categories.size() > 0) {
      criteria.andOperator(
              where("board_master_id").is(boardMasterId),
              where("id").gt(boardId),
              where("is_use").is(true),
              where("is_set_notice").is(false),
              where("category").in(categories)
      );
    } else {
      criteria.andOperator(
              where("board_master_id").is(boardMasterId),
              where("id").gt(boardId),
              where("is_use").is(true),
              where("is_set_notice").is(false)
      );
    }
    if (StringUtils.hasLength(keyword)) {
      if (searchCategory.equals("1")) {  // 제목 + 본문
        criteria.orOperator(
                where("title").regex(".*" + keyword.toLowerCase() + ".*", "i"),
                where("contents").regex(".*" + keyword.toLowerCase() + ".*", "i")
        );
      } else if(searchCategory.equals("2")) { // 제목
        criteria.orOperator(
                where("title").regex(".*" + keyword.toLowerCase() + ".*", "i")
        );
      } else if(searchCategory.equals("3")){  // 해시태그
        criteria.orOperator(
                where("tags").regex(".*" + keyword.toLowerCase() + ".*", "i")
        );
      }
    }
    query.addCriteria(criteria);
    query.with(Sort.by("create_date").ascending());
    query.limit(1);

    return query;
  }
}
