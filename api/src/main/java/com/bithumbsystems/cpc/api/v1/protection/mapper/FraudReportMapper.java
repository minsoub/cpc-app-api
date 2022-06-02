package com.bithumbsystems.cpc.api.v1.protection.mapper;

import com.bithumbsystems.cpc.api.v1.protection.model.request.FraudReportRequest;
import com.bithumbsystems.persistence.mongodb.protection.model.entity.FraudReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FraudReportMapper {

  FraudReportMapper INSTANCE = Mappers.getMapper(FraudReportMapper.class);

  FraudReport toEntity(FraudReportRequest fraudReportRequest);
}