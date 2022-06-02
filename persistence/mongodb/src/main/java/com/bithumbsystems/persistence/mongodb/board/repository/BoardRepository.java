package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BoardRepository extends ReactiveMongoRepository<Board, Long> {

  Flux<Board> findAllBy(Example<Board> example);
}
