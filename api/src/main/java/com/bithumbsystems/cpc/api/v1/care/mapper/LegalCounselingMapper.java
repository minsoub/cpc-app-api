package com.bithumbsystems.cpc.api.v1.care.mapper;

import com.bithumbsystems.cpc.api.v1.care.model.request.LegalCounselingRequest;
import com.bithumbsystems.persistence.mongodb.care.entity.LegalCounseling;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LegalCounselingMapper {

  LegalCounselingMapper INSTANCE = Mappers.getMapper(LegalCounselingMapper.class);

  LegalCounseling toEntity(LegalCounselingRequest legalCounselingRequest);
}