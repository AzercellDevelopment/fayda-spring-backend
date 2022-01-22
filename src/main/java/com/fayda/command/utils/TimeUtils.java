package com.fayda.command.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
  public static LocalDateTime now() {
    return LocalDateTime.now(ZoneId.of("GMT+04:00"));
  }
}
