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
@RequiredArgsConstructor
@Profile("dev|qa|prod|eks-dev")
@EnableSchedulerLock(defaultLockAtMostFor = "PT10S")
public class SchedulerConfig {

  private final ParameterStoreConfig config;

  @Bean
  public MongoClient mongoClient() {

    String str = String.format("mongodb://%s:%s@%s:%s",
        config.getMongoProperties().getMongodbUser(),
        config.getMongoProperties().getMongodbPassword(),
        config.getMongoProperties().getMongodbUrl(),
        config.getMongoProperties().getMongodbPort()
    );

    return MongoClients.create(str);
  }

  @Bean
  public LockProvider lockProvider(MongoClient mongoClient) {
    return new ReactiveStreamsMongoLockProvider(mongoClient.getDatabase(config.getMongoProperties().getMongodbName()));
  }

}
