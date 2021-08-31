package com.northone.challenge.controller;

import com.northone.challenge.model.Status;
import com.northone.challenge.model.Task;
import com.northone.challenge.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(SearchControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void clearDb() {
        taskRepository.deleteAll();
        taskRepository.save(new Task("desc","title", LocalDateTime.now(), Status.DONE));
    }

    @Test
    void itSearchesAndFindsMatchingTasks() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/tasks/search?description=desc&title=title")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tasks[0].title").value("title"))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        LOG.info("Task Search response: {}", actualResponseBody);
    }

    @Test
    void itSearchesAndDoesntFindNonMatchingTasks() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/tasks/search?description=descxx&title=title")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tasks").isEmpty())
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        LOG.info("Empty Task Search response: {}", actualResponseBody);
    }
}
