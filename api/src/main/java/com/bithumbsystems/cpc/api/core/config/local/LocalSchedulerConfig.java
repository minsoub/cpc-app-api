package com.bithumbsystems.cpc.api.core.config.local;


import com.mongodb.reactivestreams.client.MongoClient;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.reactivestreams.ReactiveStreamsMongoLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Profile("local")
@EnableSchedulerLock(defaultLockAtMostFor = "PT10S")
public class LocalSchedulerConfig {

  private final LocalMongoConfig config;

  @Bean
  public LockProvider lockProvider(MongoClient mongoClient) {
    return new ReactiveStreamsMongoLockProvider(mongoClient.getDatabase(config.getDatabaseName()));
  }

}
