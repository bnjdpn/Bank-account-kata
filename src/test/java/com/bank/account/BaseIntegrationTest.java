package com.bank.account;

import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/application.properties")
public abstract class BaseIntegrationTest {

    @Inject
    protected MongoTemplate mongoTemplate;

    @Before
    @After
    public void dropMongoCollections() {
        mongoTemplate.getCollectionNames().forEach(mongoTemplate::dropCollection);
    }
}
