package com.fayda.command.controller;

import com.fayda.command.dto.GenericResponse;
import com.fayda.command.dto.points.BalanceDto;
import com.fayda.command.dto.points.PointsSyncRequestDto;
import com.fayda.command.services.PointsService;
import com.fayda.command.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointsController {

  private final PointsService pointsService;

  @GetMapping("/balance")
  public ResponseEntity<GenericResponse<BalanceDto>> getBalance(@RequestAttribute(JwtUtils.ATTR_USERNAME) String userId) {
    log.info("Getting balance");
    final var res = pointsService.getBalance(UUID.fromString(userId));
    log.info("Getting balance was successful");
    return ResponseEntity.ok(GenericResponse.success(res));
  }

  @PostMapping("/sync")
  public ResponseEntity<GenericResponse<String>> syncPoints(@RequestAttribute(JwtUtils.ATTR_USERNAME) String userId,
                                                            @RequestBody PointsSyncRequestDto request) {
    log.info("Syncing user balance");
    request.setUserId(UUID.fromString(userId));
    pointsService.syncPoints(request);
    log.info("Sync complete");
    return ResponseEntity.ok(GenericResponse.success("success"));
  }
}
