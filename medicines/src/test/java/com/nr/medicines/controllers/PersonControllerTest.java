package com.nr.medicines.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nr.medicines.Utils;
import com.nr.medicines.models.Person;
import com.nr.medicines.models.dto.PersonRequest;
import com.nr.medicines.repositories.PersonsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-junit.properties"
)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonsRepository personsRepository;

    @AfterEach
    public void resetDb() {
        personsRepository.deleteAll();
    }

    @Test
    void addPerson() throws Exception {
        Person person = Utils.createPersonObj();
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName(person.getName());

        mockMvc
                .perform(
                        post("/persons/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(personRequest)))
                .andExpect(status().isOk());

        List<Person> found = personsRepository.findAll();
        assertEquals(person.getName(), found.get(0).getName());
    }

    @Test
    void getAllPersons() throws Exception {
        Person person = Utils.createPersonObj();
        personsRepository.save(person);

        mockMvc
                .perform(get("/persons/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(1))))
                .andExpect(jsonPath("$[0].name", is(person.getName())));

    }

    @Test
    void deletePerson() throws Exception {
        Person person = Utils.createPersonObj();
        personsRepository.save(person);

        mockMvc
                .perform(delete("/persons/"+person.getPersonUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Person> found = personsRepository.findAll();
        assertEquals(Collections.emptyList(), found);
    }
}
