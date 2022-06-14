package com.bithumbsystems.cpc.api.v1.board.mapper;

import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardMapper {

  BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);


  @Mapping(target = "createDate", ignore = true)
  BoardResponse toDto(Board board);
}
