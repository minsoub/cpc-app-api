package com.bithumbsystems.persistence.mongodb.shedlock.model.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "shedlock")
public class Shedlock {

  @Id
  private String name;
  private LocalDateTime lockUntil;
  private LocalDateTime lockedAt;
  private String lockedBy;

}
