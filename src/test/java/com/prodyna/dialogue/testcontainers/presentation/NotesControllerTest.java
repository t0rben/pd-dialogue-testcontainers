package com.prodyna.dialogue.testcontainers.presentation;

import com.jayway.jsonpath.JsonPath;
import com.prodyna.dialogue.testcontainers.DockerImages;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = NotesControllerTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CacheManager cacheManager;

    public static GenericContainer redis = new GenericContainer(DockerImages.REDIS).withExposedPorts(6379);

    public static GenericContainer mongo = new GenericContainer(DockerImages.MONGO).withCommand("mongod", "--port", "27017").withExposedPorts(27017);

    @ClassRule
    public static TestRule ruleChain = RuleChain.outerRule(redis).around(mongo);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        String mongoUri = "mongodb://" + mongo.getContainerIpAddress() + ":" + mongo.getMappedPort(27017);

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues.of(
                    "spring.redis.host:" + redis.getContainerIpAddress(),
                    "spring.redis.port:" + redis.getMappedPort(6379),
                    "spring.data.mongodb.uri:" + mongoUri
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Before
    public void resetDependencies() {

        mongoTemplate.remove(new Query(), "note");

        for (String cacheName : cacheManager.getCacheNames()) {
            cacheManager.getCache(cacheName).clear();
        }

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