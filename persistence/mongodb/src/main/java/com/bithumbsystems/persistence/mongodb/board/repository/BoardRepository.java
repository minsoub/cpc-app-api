package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BoardRepository extends ReactiveCrudRepository<Board, String> {
  Flux<Board> findByBoardMasterId(String boardMasterId);
  Mono<Board> findByIdAndBoardMasterId(String boardId, String boardMasterId);
}
