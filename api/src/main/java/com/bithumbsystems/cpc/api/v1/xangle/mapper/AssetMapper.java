package com.bithumbsystems.cpc.api.v1.xangle.mapper;

import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse.Data.Assets;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssetMapper {

  AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

  List<Asset> toEntity(List<Assets> assetsList);

  default List<Asset> toEntity(AssetResponse assetResponse) {
    return toEntity(assetResponse.getData().getAssets());
  }

}
