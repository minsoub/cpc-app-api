package com.bithumbsystems.persistence.mongodb.asset.service;

import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.asset.repository.AssetRepository;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import com.bithumbsystems.persistence.mongodb.disclosure.repository.DisclosureRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetDomainService {

  private final AssetRepository assetRepository;

  public Flux<Asset> saveAll(List<Asset> assetList) {
    return assetRepository.saveAll(assetList);
  }

  public Flux<Asset> findAllByProjectNameIsNull() {
    return assetRepository.findAllByProjectNameIsNull();
  }

  public Mono<Asset> save(Asset asset) {
    return assetRepository.save(asset);
  }

  public Mono<Asset> findById(String id) {
    return assetRepository.findById(id);
  }

  public Mono<Asset> findAssetByAssetId(String assetId) {
    return assetRepository.findAssetByAssetId(assetId);
  }

  public Flux<Asset> findAll() {
    return assetRepository.findAll();
  }

}
