package com.shkvlnc.ShoeApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long testOrderId;

    @BeforeEach
    void setup() throws Exception {
        // Create a fresh order before each test
        String response = mockMvc.perform(post("/api/orders")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":2}")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = JsonPath.read(response, "$.id");
        this.testOrderId = id.longValue();
    }

    // ✅ USER should NOT update order
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userCannotUpdateOrder() throws Exception {
        mockMvc.perform(put("/api/orders/{id}", testOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isForbidden());
    }

    // ✅ USER should NOT delete order
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userCannotDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", testOrderId))
                .andExpect(status().isForbidden());
    }

    // ✅ ADMIN can update order
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanUpdateOrder() throws Exception {
        mockMvc.perform(put("/api/orders/{id}", testOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    // ✅ ADMIN can delete order
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", testOrderId))
                .andExpect(status().isNoContent());
    }
}
