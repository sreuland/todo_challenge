package com.northone.challenge.repository;

import com.northone.challenge.model.Status;
import com.northone.challenge.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
class TaskRepositoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(TaskRepositoryTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void clearDb() {
        taskRepository.deleteAll();
    }

    @Test
    void itListsTasks() throws Exception {

        taskRepository.save(new Task(null,"desc","title", LocalDate.now(), Status.DONE));

        MvcResult mvcResult = mockMvc.perform(get("/api/tasks")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tasks[0].title").value("title"))
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        LOG.info("Task List reesponse: {}", actualResponseBody);

    }
}
