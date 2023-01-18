package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BoardRepository extends ReactiveMongoRepository<Board, Long> {

//  @Query("{$and: "
//        + "["
//          + "{boardMasterId: ?0}, "
//          + "{isUse: ?1}, "
//          + "{title: {$regex: ?2, $options: 'i'}}, "
//          + "{$or: "
//            + "["
//              + "{$expr: {$eq: ['?3', '[]']}}, "
//              + "{category: {$in : ?3}}"
//            + "]"
//          + "}"
//        + "]"
//      + "}")
//  Flux<Board> findByConditionWithCategory(String boardMasterId, Boolean isUse, String keyword, List<String> categories);

  @Query(value = "{$and: "
        + "["
            + "{ boardMasterId :  ?0 }, "
            + "{ isUse : true }, "
            + "{ isSetNotice : true }, "
        + "],"
      + "}",
      sort = "{ create_date : -1 }")
  Flux<Board> findALLByIsSetNotice(String boardMasterId);

  Flux<Board> findBoardByBoardMasterIdAndIsUseOrderByCreateDateDesc(String BoardMasterId, Boolean isUse);

  //@Query(value = "{$end: { boardMasterId :  { $ne:  'CPC_NOTICE'}}}")
  @Aggregation(pipeline = {
          "{ '$match':  {$end:  [{ boardMasterId: {$ne:  'CPC_NOTICE' }}, {isUse:  true} ] }}",
          "{ '$sort':  { create_date:  -1}}",
          "{ '$limit':  ?0 }"
  })
  Flux<Board> findBoardByIsUseOrderByCreateDateDesc(int limit, Boolean isUse);

  Mono<Board> findBoardByIdAndIsUse(Long id, Boolean isUse);
}
