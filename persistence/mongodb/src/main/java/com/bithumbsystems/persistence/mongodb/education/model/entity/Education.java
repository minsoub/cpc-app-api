package com.bithumbsystems.persistence.mongodb.education.model.entity;

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
@Document(collection = "cpc_education")
public class Education {

  @Id
  private String id;
  private String name;
  private String email;
  private String cellPhone;
  private String content;
  private LocalDateTime desireDate;
  private Boolean isAnswerComplete;
  //private Boolean isConsignmentAgreement;
  private Boolean isUseAgreement;
  private Boolean isEmail;
  private String answer;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private String updateAccountId;

}
