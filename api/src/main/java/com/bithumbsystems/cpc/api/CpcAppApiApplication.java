package com.bithumbsystems.cpc.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;


@SpringBootApplication(scanBasePackages = "com.bithumbsystems", exclude = {
    MongoAutoConfiguration.class,
    MongoReactiveAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    EmbeddedMongoAutoConfiguration.class
})
@EnableWebFlux
@ConfigurationPropertiesScan("com.bithumbsystems.cpc.api.core.config")
@EnableReactiveMongoRepositories("com.bithumbsystems.persistence.mongodb")
@OpenAPIDefinition(info = @Info(title = "Customer Protection Center API", version = "1.0", description = "Customer Protection Center APIs v1.0"))
public class CpcAppApiApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(CpcAppApiApplication.class);
    app.addListeners(new ApplicationPidFileWriter());
    app.run(args);
  }
}
