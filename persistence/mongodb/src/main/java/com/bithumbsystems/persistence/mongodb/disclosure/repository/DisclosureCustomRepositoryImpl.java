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
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DisclosureCustomRepositoryImpl implements DisclosureCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  private final AssetRepository assetRepository;

  @Override
  public Flux<Disclosure> findByOrderByPublishTimestampDesc(String searchCategory, String search, Pageable pageable) {
    return reactiveMongoTemplate.aggregate(getAggregationDisclosureWithAsset(searchCategory, search, pageable), "cpc_disclosure", Disclosure.class);
  }

  @Override
  public Mono<Long> countBySearchText(String searchCategory, String search, Pageable pageable) {
    return reactiveMongoTemplate.aggregate(getAggregationDisclosureWithAssetWithoutPageable(searchCategory, search), "cpc_disclosure", Disclosure.class).count();
  }

  private Aggregation getAggregationDisclosureWithAsset(String searchCategory, String search, Pageable pageable) {

    Criteria criteria = new Criteria();

    if (StringUtils.hasLength(search)) {
        if (searchCategory.equals("1")) {  // 제목 + 심볼
            criteria.orOperator(
                    where("project_symbol").regex(".*" + search.toUpperCase() + ".*", "i"),
                    where("title").regex(".*" + search.toLowerCase() + ".*", "i")
            );
        } else if(searchCategory.equals("2")) { // 프로젝트명
            criteria.orOperator(
                    where("cpc_asset.project_name").regex(".*" + search.toUpperCase() + ".*", "i")
            );
        } else if (searchCategory.equals("3")) {  // 제목
            criteria.orOperator(
                    where("title").regex(".*" + search.toLowerCase() + ".*", "i")
            );
        }
    }
    criteria.andOperator(
            where("cpc_asset.project_name").exists(true),
            where("cpc_asset.project_name").ne(false)
    );
    MatchOperation matchOperation = Aggregation.match(criteria);
    LookupOperation lookupOperation = Aggregation.lookup("cpc_asset", "project_symbol", "_id", "cpc_asset");
    SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "publish_timestamp"));
    SkipOperation skipOperation = Aggregation.skip(pageable.getPageNumber() * pageable.getPageSize());
    LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());


    return Aggregation.newAggregation(
        lookupOperation,
        matchOperation,
        sortOperation,
        skipOperation,
        limitOperation
    );
  }

  private Aggregation getAggregationDisclosureWithAssetWithoutPageable(String searchCategory, String search) {

    Criteria criteria = new Criteria();

    if (StringUtils.hasLength(search)) {
      if (searchCategory.equals("1")) {  // 제목 + 심볼
        criteria.orOperator(
                where("project_symbol").regex(".*" + search.toUpperCase() + ".*", "i"),
                where("title").regex(".*" + search.toLowerCase() + ".*", "i")
        );
      } else if(searchCategory.equals("2")) { // 프로젝트명
        criteria.orOperator(
                where("project_symbol").regex(".*" + search.toUpperCase() + ".*", "i")
        );
      } else if (searchCategory.equals("3")) {  // 제목
        criteria.orOperator(
                where("title").regex(".*" + search.toLowerCase() + ".*", "i")
        );
      }
    }
    criteria.andOperator(
            where("cpc_asset.project_name").exists(true),
            where("cpc_asset.project_name").ne(false)
    );
    MatchOperation matchOperation = Aggregation.match(criteria);
    LookupOperation lookupOperation = Aggregation.lookup("cpc_asset", "project_symbol", "_id", "cpc_asset");

    return Aggregation.newAggregation(
        lookupOperation,
        matchOperation
    );
  }
}
