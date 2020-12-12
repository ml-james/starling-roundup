package com.starling.roundupservice;

import com.starling.roundupservice.common.account.roundup.RoundupAccountRepository;
import com.starling.roundupservice.common.account.roundup.RoundupStateRepository;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "60000")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class BaseTestIT extends BaseTest
{
    protected static final String PATH_SAVE_ROUNDUP = "/saveRoundupGoal/accountUid/%s/";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected static RoundupAccountRepository roundupAccountRepository;
    @Autowired
    protected static RoundupStateRepository roundupStateRepository;

    protected MockedParameters mockedParameters;

    @Value("${mock.server.port}")
    protected int mockServerPort;

    protected MockWebServer server = new MockWebServer();

    @BeforeEach
    void setUp() throws IOException {
        server.start(mockServerPort);
    }

    @AfterEach
    void tearDown() throws IOException
    {
        server.shutdown();
    }
}
