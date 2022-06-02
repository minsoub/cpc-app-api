package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
  Flux<Comment> findByBoardId(Long boardId);
}
