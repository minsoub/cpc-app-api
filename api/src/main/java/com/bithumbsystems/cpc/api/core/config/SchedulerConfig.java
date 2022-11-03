package com.bithumbsystems.cpc.api.core.config;


import com.bithumbsystems.cpc.api.core.config.property.AwsProperties;
import com.bithumbsystems.cpc.api.core.config.property.MongoProperties;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.reactivestreams.ReactiveStreamsMongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Data
@Slf4j
@Profile("dev|qa|prod|eks-dev")
@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT10S")
public class SchedulerConfig {

  private final MongoProperties properties;

  public SchedulerConfig(MongoProperties properties) {
    this.properties = properties;
  }

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create("mongodb://" + properties.getMongodbUrl() + ":" + properties.getMongodbPort());
  }

  @Bean
  @Profile("!local")
  public LockProvider lockProvider(MongoClient mongoClient) {
    return new ReactiveStreamsMongoLockProvider(mongoClient.getDatabase(properties.getMongodbName()));
  }

}


