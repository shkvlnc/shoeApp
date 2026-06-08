package com.shkvlnc.ShoeApp;

import com.shkvlnc.ShoeApp.dto.OrderRequestDTO;
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
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long testOrderId;

    @BeforeEach
    void setup() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(1L, 2);

        String response = mockMvc.perform(post("/api/orders")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Convert Integer → Long safely
        Integer id = JsonPath.read(response, "$.id");
        this.testOrderId = id.longValue();
    }


    // ✅ USER: Place Order
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void placeOrder_shouldReturnCreatedOrder() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(1L, 2);

        mockMvc.perform(post("/api/orders")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // ✅ USER: Get Orders
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUserOrders_shouldReturnOrders() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ✅ ADMIN: Update Order
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateOrderStatus_shouldReturnUpdatedOrder() throws Exception {
        mockMvc.perform(put("/api/orders/{id}", testOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    // ✅ ADMIN: Delete Order
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteOrder_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}", testOrderId))
                .andExpect(status().isNoContent());
    }
}
