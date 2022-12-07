package com.bithumbsystems.cpc.api.core.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "xangle")
@Getter
@Setter
public class XangleProperties {

  private String host;
  private String disclosurePath;
  private String assetListPath;
  private String assetProfilePath;
  private String xangleApiKey;



}
