package com.bithumbsystems.persistence.mongodb.board.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<Board> findPageBySearchText(String boardMasterId, String keyword, List<String> categories, Pageable pageable) {
    return reactiveMongoTemplate.find(getQueryBySearchText(boardMasterId, keyword, categories).with(pageable), Board.class);
  }

  @Override
  public Mono<Long> countBySearchText(String boardMasterId, String keyword, List<String> categories) {
    return reactiveMongoTemplate.count(getQueryBySearchText(boardMasterId, keyword, categories), Board.class);
  }

  private Query getQueryBySearchText(String boardMasterId, String keyword, List<String> categories) {
    var query = new Query();

    if (categories.size() > 0) {
      query.addCriteria(
          new Criteria()
              .andOperator(
                  where("board_master_id").is(boardMasterId),
                  where("is_use").is(true),
                  where("title").regex(".*" + keyword.toLowerCase() + ".*", "i"),
                  where("category").in(categories)
              )
      );
    } else {
      query.addCriteria(
          new Criteria()
              .andOperator(
                  where("board_master_id").is(boardMasterId),
                  where("is_use").is(true),
                  where("title").regex(".*" + keyword.toLowerCase() + ".*", "i")
              )
      );
    }
    return query;
  }
}
