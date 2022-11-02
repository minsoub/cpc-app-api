package com.bithumbsystems.persistence.mongodb.disclosure.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.bithumbsystems.persistence.mongodb.asset.repository.AssetRepository;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardCustomRepository;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import java.net.URLDecoder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DisclosureCustomRepositoryImpl implements DisclosureCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  private final AssetRepository assetRepository;

  @Override
  public Flux<Disclosure> findByOrderByPublishTimestampDesc(String search, Pageable pageable) {
    Criteria criteria = new Criteria();

    criteria.orOperator(
        where("project_symbol").regex(".*" + search.toUpperCase() + ".*", "i"),
        where("title").regex(".*" + search.toLowerCase() + ".*", "i"),
        where("cpc_asset.project_name").regex(".*" + search.toLowerCase() + ".*", "i")
    );

    MatchOperation matchOperation = Aggregation.match(criteria);
    LookupOperation lookupOperation = Aggregation.lookup("cpc_asset", "project_symbol", "_id", "cpc_asset");
    SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "publish_timestamp"));
    SkipOperation skipOperation = Aggregation.skip(pageable.getPageNumber() * pageable.getPageSize());
    LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());
    Aggregation aggregation = Aggregation.newAggregation(
        lookupOperation,
        matchOperation,
        sortOperation,
        skipOperation,
        limitOperation
    );

    return reactiveMongoTemplate.aggregate(aggregation, "cpc_disclosure", Disclosure.class);
  }
}
