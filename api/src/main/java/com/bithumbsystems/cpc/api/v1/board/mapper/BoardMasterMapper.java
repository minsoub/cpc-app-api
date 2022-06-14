package com.bithumbsystems.cpc.api.v1.board.mapper;

import com.bithumbsystems.cpc.api.v1.board.model.response.BoardMasterResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardMasterMapper {

  BoardMasterMapper INSTANCE = Mappers.getMapper(BoardMasterMapper.class);


  BoardMasterResponse toDto(BoardMaster boardMaster);
}