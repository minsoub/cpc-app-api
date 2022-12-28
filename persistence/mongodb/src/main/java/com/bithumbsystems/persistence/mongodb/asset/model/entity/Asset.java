package com.bithumbsystems.persistence.mongodb.asset.model.entity;

import com.bithumbsystems.persistence.mongodb.base.entity.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
@Document(collection = "cpc_asset")
public class Asset extends Date {

  @Id
  private String symbol;
  private String assetId;
  private String name;
  @Setter
  private String projectName;
  @Setter
  private String assetName;
  @Builder.Default
  private Boolean isSymbolMatching = true;

}
