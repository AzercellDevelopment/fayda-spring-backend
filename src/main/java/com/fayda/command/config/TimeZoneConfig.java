package com.fayda.command.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

  @Autowired
  public void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+4:00"));
  }
}
