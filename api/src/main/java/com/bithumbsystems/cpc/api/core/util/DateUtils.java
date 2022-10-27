package com.bithumbsystems.cpc.api.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static String toString(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public static LocalDateTime toLocalDateTime(String yyyyMMdd) {
    return LocalDate.parse(yyyyMMdd, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
  }

}
