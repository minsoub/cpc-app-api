package com.bithumbsystems.cpc.api.v1.xangle.mapper;

import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureClientResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.DisclosureResponse.Data.Disclosures;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import com.bithumbsystems.persistence.mongodb.disclosure.model.entity.Disclosure;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DisclosureDateMapper.class)
public interface DisclosureResponseMapper {

  DisclosureResponseMapper INSTANCE = Mappers.getMapper(DisclosureResponseMapper.class);

  @Mapping(target = "symbol", source = "disclosure.id")
  @Mapping(target = "createDate", source = "disclosure.publishTimestamp")
  DisclosureClientResponse entityToResponse(Disclosure disclosure, Asset asset);


}
