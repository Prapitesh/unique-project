
package com.prapitesh.distributedratelimiter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class RateLimiterFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTooManyRequestsWhenBucketIsFull() throws Exception {
        String clientId = "filter-test-" + java.util.UUID.randomUUID();

        MvcResult first = mockMvc.perform(
                get("/api/test").header("X-Client-Id", clientId)
        ).andReturn();

        MvcResult second = mockMvc.perform(
                get("/api/test").header("X-Client-Id", clientId)
        ).andReturn();

        MvcResult third = mockMvc.perform(
                get("/api/test").header("X-Client-Id", clientId)
        ).andReturn();

        MvcResult fourth = mockMvc.perform(
                get("/api/test").header("X-Client-Id", clientId)
        ).andReturn();

        assertEquals(200, first.getResponse().getStatus());
        assertEquals(200, second.getResponse().getStatus());
        assertEquals(200, third.getResponse().getStatus());

        assertEquals(429, fourth.getResponse().getStatus());

        assertTrue(
                fourth.getResponse().getContentAsString()
                        .contains("Too many requests")
        );
    }
}
