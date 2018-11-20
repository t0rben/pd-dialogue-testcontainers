package com.prodyna.dialogue.testcontainers;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;

@ContextConfiguration(initializers = AbstractDependencies.Initializer.class)
public class AbstractDependencies {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CacheManager cacheManager;

    public static GenericContainer redis = new GenericContainer("redis:5.0.1-alpine").withExposedPorts(6379);

    public static GenericContainer mongo = new GenericContainer("mongo:3.6.9").withCommand("mongod", "--port", "27017").withExposedPorts(27017);

    @ClassRule
    public static TestRule ruleChain = RuleChain.outerRule(redis).around(mongo);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        String mongoUri = "mongodb://" + mongo.getContainerIpAddress() + ":" + mongo.getMappedPort(27017);

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues.of(
                    "spring.redis.host:" + redis.getContainerIpAddress(),
                    "spring.redis.port:" + redis.getMappedPort(6379),
                    "spring.data.mongodb.uri:" + mongoUri
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Before
    public void resetDependencies() {

        mongoTemplate.remove(Note.class);

        for(String cacheName : cacheManager.getCacheNames()) {
            cacheManager.getCache(cacheName).clear();
        }
    }
}
