package com.bithumbsystems.cpc.api.v1.board.mapper;

import com.bithumbsystems.cpc.api.v1.board.model.response.BoardResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.Board;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardMapper {

  BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

  @Mapping(target = "category", source = "category", qualifiedByName = "listToString")
  BoardResponse toDto(Board board);


  @Mapping(target = "contents", ignore = false)
  @Mapping(target = "category", source = "category", qualifiedByName = "listToString")
  BoardResponse toDtoWithoutContents(Board board);

  @Named("listToString")
  default String listToString(List<String> category) {
    if (category != null) {
      return category.stream()
          .map(String::valueOf)
          .collect(Collectors.joining("/"));
    }
    return "";
  }


}
