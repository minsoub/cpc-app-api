package com.bithumbsystems.cpc.api.v1.xangle.controller;

import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.DISCLOSURE_DEFAULT_PAGE_SIZE;
import static com.bithumbsystems.cpc.api.core.config.constant.GlobalConstant.FIRST_PAGE_NUM;

import com.bithumbsystems.cpc.api.core.model.response.MultiResponse;
import com.bithumbsystems.cpc.api.core.model.response.SingleResponse;
import com.bithumbsystems.cpc.api.v1.xangle.service.DisclosureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/disclosure")
public class DisclosureController {

  private final DisclosureService disclosureService;

  @GetMapping("/list")
  public ResponseEntity<?> getDisclosureList(
      @RequestParam(name = "query", required = false, defaultValue = "") String query,
      @RequestParam(name = "page_no", defaultValue = FIRST_PAGE_NUM) int pageNo,
      @RequestParam(name = "page_size", defaultValue = DISCLOSURE_DEFAULT_PAGE_SIZE) int pageSize) {
    return ResponseEntity.ok().body(disclosureService.getDisclosureList(query, pageNo, pageSize).map(
        SingleResponse::new));
  }

}
