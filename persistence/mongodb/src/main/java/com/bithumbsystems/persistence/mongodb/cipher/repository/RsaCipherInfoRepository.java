package com.bithumbsystems.persistence.mongodb.cipher.repository;

import com.bithumbsystems.persistence.mongodb.cipher.entity.RsaCipherInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RsaCipherInfoRepository extends ReactiveMongoRepository<RsaCipherInfo, String> {
}
