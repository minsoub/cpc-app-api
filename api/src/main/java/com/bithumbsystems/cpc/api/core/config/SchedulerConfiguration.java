//package com.bithumbsystems.cpc.api.core.config;
//
//
//import com.mongodb.reactivestreams.client.MongoClient;
//import net.javacrumbs.shedlock.core.LockProvider;
//import net.javacrumbs.shedlock.provider.mongo.reactivestreams.ReactiveStreamsMongoLockProvider;
//import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
//@Configuration
//@EnableScheduling
//@EnableSchedulerLock(defaultLockAtMostFor = "PT10S")
//public class SchedulerConfiguration {
//
//  @Bean
//  public LockProvider lockProvider(MongoClient mongoClient) {
//    return new ReactiveStreamsMongoLockProvider(mongoClient.getDatabase("test"));
//  }
//
//}
