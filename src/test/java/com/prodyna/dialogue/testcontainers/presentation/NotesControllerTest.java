package com.prodyna.dialogue.testcontainers.presentation;

import com.prodyna.dialogue.testcontainers.AbstractDependencies;
import com.prodyna.dialogue.testcontainers.business.NoteService;
import com.prodyna.dialogue.testcontainers.configuration.MapperConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class NotesControllerTest extends AbstractDependencies {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllNotes() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/notes")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createNote() {

    }

    @Test
    public void getNote() {

    }

    @Test
    public void updateNote() {

    }

    @Test
    public void deleteNote() {

    }
}