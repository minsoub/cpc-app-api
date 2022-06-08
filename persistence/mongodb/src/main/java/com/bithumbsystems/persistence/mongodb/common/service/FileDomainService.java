package com.bithumbsystems.persistence.mongodb.common.service;

import com.bithumbsystems.persistence.mongodb.common.model.entity.File;
import com.bithumbsystems.persistence.mongodb.common.repository.FileRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FileDomainService {

  private final FileRepository fileRepository;

  /**
   * File save in MongoDB
   * @param info
   * @return
   */
  public Mono<File> save(File info) {
    info.setCreateDate(LocalDateTime.now());
    return fileRepository.save(info);
  }
}
