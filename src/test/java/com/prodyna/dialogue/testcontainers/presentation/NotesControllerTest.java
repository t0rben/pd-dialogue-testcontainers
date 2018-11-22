package com.prodyna.dialogue.testcontainers.presentation;

import com.jayway.jsonpath.JsonPath;
import com.prodyna.dialogue.testcontainers.AbstractDependencies;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class NotesControllerTest extends AbstractDependencies {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setupTest() {

        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAllNotes() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/notes")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createNote() throws Exception {

        NoteIncomingDTO noteIncomingDTO = new NoteIncomingDTO();
        noteIncomingDTO.setContent("Quite impressive");

        mockMvc.perform(MockMvcRequestBuilders.post("/notes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(noteIncomingDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(Matchers.is("Quite impressive")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastModifiedDate").exists());

    }

    @Test
    public void getNote() throws Exception {

        createNote();

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/notes")).andReturn();

        final String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.notes[0].id");

        mockMvc.perform(MockMvcRequestBuilders.get("/notes/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(Matchers.is("Quite impressive")));

    }

    @Test
    public void updateNote() throws Exception {

        createNote();

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/notes")).andReturn();

        final String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.notes[0].id");

        NoteIncomingDTO noteIncomingDTO = new NoteIncomingDTO();
        noteIncomingDTO.setContent("Quite more impressive");

        mockMvc.perform(MockMvcRequestBuilders.put("/notes/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(noteIncomingDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(Matchers.is("Quite more impressive")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value(Matchers.is(1)));

    }

    @Test
    public void deleteNote() throws Exception {

        createNote();

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/notes")).andReturn();

        final String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.notes[0].id");

        mockMvc.perform(MockMvcRequestBuilders.delete("/notes/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/notes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.notes").isEmpty());

    }
}