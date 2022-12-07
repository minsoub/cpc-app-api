package com.bithumbsystems.cpc.api.v1.xangle.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DisclosureDateMapper {

  public LocalDateTime asDate(String date) {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
    return LocalDateTime.parse(date, formatter);

//    try {
//      return date != null ? new SimpleDateFormat( "yyyy-MM-dd" ).parse( date ) : null;
//    }
//    catch ( ParseException e ) {
//      throw new RuntimeException( e );
//    }
  }

}
