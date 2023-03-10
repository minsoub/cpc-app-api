package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BoardCustomRepository {

  Flux<Board> findPageBySearchText(String boardMasterId, String keyword, List<String> categories, Pageable pageable);

  Mono<Long> countBySearchText(String boardMasterId, String keyword, List<String> categories);
}
