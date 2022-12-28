package com.bithumbsystems.cpc.api.v1.main.mapper;

import com.bithumbsystems.cpc.api.v1.main.model.response.BtciClientResponse;
import com.bithumbsystems.cpc.api.v1.main.model.response.BtciResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BtciResponseMapper {

  BtciResponseMapper INSTANCE = Mappers.getMapper(BtciResponseMapper.class);

  @Mapping(target = "altcoinMarketIndex.marketIndex", source = "btciResponse.data.btai.market_index")
  @Mapping(target = "altcoinMarketIndex.width", source = "btciResponse.data.btai.width")
  @Mapping(target = "altcoinMarketIndex.rate", source = "btciResponse.data.btai.rate")
  @Mapping(target = "marketIndex.marketIndex", source = "btciResponse.data.btmi.market_index")
  @Mapping(target = "marketIndex.width", source = "btciResponse.data.btmi.width")
  @Mapping(target = "marketIndex.rate", source = "btciResponse.data.btmi.rate")
  @Mapping(target = "date", source = "btciResponse.data.date", qualifiedByName = "longToLocalDate")
  BtciClientResponse toDto(BtciResponse btciResponse);

  @Named("longToLocalDate")
  default LocalDateTime longToLocalDate(Long date) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), TimeZone
        .getDefault().toZoneId());
  }

}