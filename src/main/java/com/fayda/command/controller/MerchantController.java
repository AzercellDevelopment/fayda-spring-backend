package com.fayda.command.controller;

import com.fayda.command.dto.GenericResponse;
import com.fayda.command.dto.merchants.GroupedMerchantResponse;
import com.fayda.command.services.MerchantService;
import com.fayda.command.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant")
public class MerchantController {

  private final MerchantService merchantService;

  @GetMapping("/all")
  public ResponseEntity<GenericResponse<GroupedMerchantResponse>> getAllGrouped(
      @RequestAttribute(JwtUtils.ATTR_USERNAME) String userId) {
    log.info("Getting all merchants");
    final var res = merchantService.getAllMerchants(UUID.fromString(userId));
    log.info("All merchants fetched");
    return ResponseEntity.ok(GenericResponse.success(res));
  }

  @PostMapping("/start")
  public ResponseEntity<GenericResponse<String>> startTask(
      @RequestAttribute(JwtUtils.ATTR_USERNAME) String userId,
      @RequestParam("merchant_id") UUID merchantId) {
    log.info("Starting {} task", merchantId);
    final var res = merchantService.startTask(UUID.fromString(userId), merchantId);
    log.info("Finished {} task", merchantId);
    return ResponseEntity.ok(GenericResponse.success(res));
  }

  @DeleteMapping("/cancel")
  public ResponseEntity<GenericResponse<String>> cancelTask(
      @RequestAttribute(JwtUtils.ATTR_USERNAME) String userId) {
    log.info("Cancelling task");
    final var res = merchantService.cancelTask(UUID.fromString(userId));
    log.info("Finished cancelling task");
    return ResponseEntity.ok(GenericResponse.success(res));
  }

  @PostMapping("/complete")
  public ResponseEntity<GenericResponse<BigDecimal>> completeTask(
      @RequestParam("merchant_id") UUID merchantId,
      @RequestParam("ref_num") String refNum) {
    log.info("Completing task");
    final var res = merchantService.completeTask(refNum, merchantId);
    log.info("Finished completion task");
    return ResponseEntity.ok(GenericResponse.success(res));
  }
}
