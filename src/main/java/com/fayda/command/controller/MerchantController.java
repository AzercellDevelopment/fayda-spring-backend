package com.fayda.command.controller;

import com.fayda.command.dto.GenericResponse;
import com.fayda.command.dto.merchants.GroupedMerchantResponse;
import com.fayda.command.services.MerchantService;
import com.fayda.command.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant")
public class MerchantController {

  private final MerchantService merchantService;

  @GetMapping("/all")
  ResponseEntity<GenericResponse<GroupedMerchantResponse>> getAllGrouped(
      @RequestAttribute(JwtUtils.ATTR_USERNAME) String userId) {
    log.info("Getting all merchants");
    final var res = merchantService.getAllMerchants(UUID.fromString(userId));
    log.info("All merchants fetched");
    return ResponseEntity.ok(GenericResponse.success(res));
  }
}
