package com.bithumbsystems.cpc.api.v1.xangle.mapper;

import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetProfileResponse.Data;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse;
import com.bithumbsystems.cpc.api.v1.xangle.response.AssetResponse.Data.Assets;
import com.bithumbsystems.persistence.mongodb.asset.model.entity.Asset;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssetMapper {

  AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

  List<Asset> toEntity(List<Assets> assetsList);

  Asset profileResponseToEntity(List<Data> assetProfileResponse, Data.Assets assetProfile);

  default List<Asset> toEntity(AssetResponse assetResponse) {
    return toEntity(assetResponse.getData().getAssets());
  }

  default Asset profileResponseToEntity(AssetProfileResponse assetProfileResponse) {
    return profileResponseToEntity(assetProfileResponse.getData(), assetProfileResponse.getData().get(0).getAssetProfile());
  }

}
