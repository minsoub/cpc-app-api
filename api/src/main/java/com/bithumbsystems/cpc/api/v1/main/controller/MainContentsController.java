package com.bithumbsystems.cpc.api.v1.main.controller;

import com.bithumbsystems.cpc.api.v1.main.service.MainContentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Tag(name = "Main APIs", description = "메인 화면 관련 API")
public class MainContentsController {

  private final MainContentsService mainContentsService;

  /**
   * 메인 화면 컨텐츠 조회
   * @return
   */
  @GetMapping("/")
  @ResponseBody
  public ResponseEntity<Mono<?>> getMainContents() {
    String id = "default";
    return ResponseEntity.ok().body(mainContentsService.getMainContents(id));
  }
}
