package com.bithumbsystems.persistence.mongodb.education.service;

import com.bithumbsystems.persistence.mongodb.education.model.entity.Education;
import com.bithumbsystems.persistence.mongodb.education.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationDomainService {

  private final EducationRepository educationRepository;

  public Mono<Education> save(Education education) {
    return educationRepository.save(education);
  }

}
