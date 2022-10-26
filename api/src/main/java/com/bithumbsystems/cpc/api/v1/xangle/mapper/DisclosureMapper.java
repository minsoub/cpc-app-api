package com.bithumbsystems.cpc.api.v1.xangle.mapper;

import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse.Data.Disclosures;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DisclosureDateMapper.class)
public interface DisclosureMapper {

  DisclosureMapper INSTANCE = Mappers.getMapper(DisclosureMapper.class);

//  @Mapping(target = "publishTimestamp", source = "publishTimestamp", dateFormat = "yyyy-MM-ddTHH:mm:ss.SSSSSSXXX")
//  @Mapping(target = "publishTimestampUtc", source = "publishTimestampUtc", dateFormat = "yyyy-MM-ddTHH:mm:ss.SSSSSS")
//  @Mapping(target = "lastModifiedTimestamp", source = "lastModifiedTimestamp", dateFormat = "yyyy-MM-ddTHH:mm:ss.SSSSSS")
  List<Disclosure> toEntity(List<Disclosures> disclosureResponse);

  default List<Disclosure> toEntity(DisclosureResponse disclosureResponse) {
    return toEntity(disclosureResponse.getData().getDisclosures());
  }

  @Named("stringToLocalDateTime")
  default LocalDateTime stringToLocalDateTime(String date) {

    System.out.println("@@@@@@@@@@@@@@");
    System.out.println(date);
    System.out.println("@@@@@@@@@@@@@@");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss.SSSSSSXXX");
    return LocalDateTime.parse(date, formatter);
  }

}
