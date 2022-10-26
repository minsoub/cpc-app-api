package com.bithumbsystems.persistence.mongodb.asset.repository;

import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends ReactiveMongoRepository<Asset, String> {
}