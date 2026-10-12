
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
    void shouldReturnBadRequestWhenClientIdIsMissing() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/test"))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertEquals(
                "{\"error\":\"X-Client-Id header is required\"}",
                result.getResponse().getContentAsString()
        );
    }

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
        assertEquals(
                "{\"error\":\"Too many requests\"}",
                fourth.getResponse().getContentAsString()
        );

        assertTrue(
                fourth.getResponse().getHeader("Retry-After") != null
        );
    }

    @Test
    void shouldKeepRateLimitsSeparateForDifferentClients() throws Exception {
        String clientA = "client-A-" + java.util.UUID.randomUUID();
        String clientB = "client-B-" + java.util.UUID.randomUUID();

        // Exhaust Client A's bucket.
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(
                    get("/api/test").header("X-Client-Id", clientA)
            ).andReturn();
        }

        // Client B should still have its own available bucket.
        MvcResult clientBResult = mockMvc.perform(
                get("/api/test").header("X-Client-Id", clientB)
        ).andReturn();

        assertEquals(200, clientBResult.getResponse().getStatus());
    }



}
