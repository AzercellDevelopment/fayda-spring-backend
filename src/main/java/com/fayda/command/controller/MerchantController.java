package com.fayda.command.controller;

import com.fayda.command.dto.GenericResponse;
import com.fayda.command.dto.merchants.GroupedMerchantResponse;
import com.fayda.command.services.MerchantService;
import com.fayda.command.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/start")
  ResponseEntity<GenericResponse<String>> startTask(
      @RequestAttribute(JwtUtils.ATTR_USERNAME) String userId,
      @RequestParam("merchant_id") UUID merchantId) {
    log.info("Starting {} task", merchantId);
    final var res = merchantService.startTask(UUID.fromString(userId), merchantId);
    log.info("Finished {} task", merchantId);
    return ResponseEntity.ok(GenericResponse.success(res));
  }
}
