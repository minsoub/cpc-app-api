package com.bithumbsystems.persistence.mongodb.main.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "cpc_main_contents")
public class MainContents {

  /**
   * ID
   */
  @Id
  private String id;

  /**
   * 가상 자산 동향
   */
  private ArrayList<Long> virtualAssetTrends;

  /**
   * 블록체인 뉴스
   */
  private ArrayList<Long> blockchainNews;

  /**
   * 투자 가이드 게시판1 ID
   */
  private String investmentGuide1Id;

  /**
   * 투자 가이드 게시판1 컨텐츠
   */
  private ArrayList<Long> investmentGuide1;

  /**
   * 투자 가이드 게시판2 ID
   */
  private String investmentGuide2Id;

  /**
   * 투자 가이드 게시판2 컨텐츠
   */
  private ArrayList<Long> investmentGuide2;

  /**
   * 투자 가이드 게시판3 ID
   */
  private String investmentGuide3Id;

  /**
   * 투자 가이드 게시판3 컨텐츠
   */
  private ArrayList<Long> investmentGuide3;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
}
