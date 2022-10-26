package com.bithumbsystems.persistence.mongodb.disclosure.model.entity;

import com.bithumbsystems.persistence.mongodb.base.entity.Date;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
@Document(collection = "cpc_disclosure")
public class Disclosure extends Date {

  @Id
  private String disclosureId;
  private String projectSymbol;
  private String projectLogo;
  private String projectId;
  private String title;
  private String publishStatus;
  private LocalDateTime publishTimestamp;
  private LocalDateTime publishTimestampUtc;
  private LocalDateTime lastModifiedTimestamp;
  private String disclosureType;
  private String disclosureContentType;
  private String validationMethod;
  private String xangleUrl;
  private String pdfFileUrl;
  private Boolean valid;
  private Boolean resolved;

}
