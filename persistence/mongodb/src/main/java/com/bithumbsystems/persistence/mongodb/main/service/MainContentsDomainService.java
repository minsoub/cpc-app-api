package com.bithumbsystems.persistence.mongodb.main.service;

import com.bithumbsystems.persistence.mongodb.main.model.entity.MainContents;
import com.bithumbsystems.persistence.mongodb.main.repository.MainContentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainContentsDomainService {

  private final MainContentsRepository mainContentsRepository;
  private final String id = "default";

  /**
   * 메인 컨텐츠 조회
   * @return
   */
  public Mono<MainContents> findOne() {
    return mainContentsRepository.findById(id);
  }
}
