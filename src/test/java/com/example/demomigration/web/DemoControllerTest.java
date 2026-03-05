package com.example.demomigration.web;

import com.example.demomigration.persistence.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * JUnit 4: @RunWith(SpringRunner.class), @Before, public @Test → JUnit 5 (Boot 3).
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    @WithMockUser
    public void testListItems() throws Exception {
        mockMvc.perform(get("/api/public/items").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetItemNotFound() throws Exception {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/public/items/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateItem() throws Exception {
        com.example.demomigration.persistence.Item item = new com.example.demomigration.persistence.Item();
        item.setId(1L);
        item.setName("Test");
        when(itemRepository.save(any())).thenReturn(item);
        mockMvc.perform(post("/api/public/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\"}"))
            .andExpect(status().isCreated());
    }
}
