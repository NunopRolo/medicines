package com.nr.medicines.services;

import com.nr.medicines.Utils;
import com.nr.medicines.models.Person;
import com.nr.medicines.models.dto.PersonRequest;
import com.nr.medicines.repositories.PersonsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonsServiceTest {
    @Mock
    private PersonsRepository personsRepository;

    private PersonsService personsService;

    @BeforeEach
    void setUp(){
        personsService = new PersonsService(personsRepository);
    }

    //create new Person
    @Test
    void createNewPerson(){
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("name");

        this.personsService.createNewPerson(personRequest);

        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personsRepository).save(personArgumentCaptor.capture());

        Person person = personArgumentCaptor.getValue();
        assertEquals(personRequest.getName(), person.getName());
    }

    //getAllPersons
    @Test
    void getAllPersons(){
        Person person = Utils.createPersonObj();

        when(this.personsRepository.findAll())
                .thenReturn(List.of(person));

        List<Person> personList = this.personsService.getAllPersons();
        assertEquals(person.getPersonUuid(), personList.get(0).getPersonUuid());
        verify(personsRepository).findAll();
    }

    //getPersonByUuid
    @Test
    void getPersonByUuid(){
        Person person = Utils.createPersonObj();

        when(this.personsRepository.findByPersonUuid(person.getPersonUuid()))
                .thenReturn(Optional.of(person));

        Person result = this.personsService.getPersonByUuid(person.getPersonUuid());
        assertEquals(result, person);
        verify(personsRepository).findByPersonUuid(person.getPersonUuid());
    }

    @Test
    void getPersonByUuid_NotFound(){
        Person person = Utils.createPersonObj();

        when(this.personsRepository.findByPersonUuid(person.getPersonUuid()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.personsService.getPersonByUuid(person.getPersonUuid()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(personsRepository).findByPersonUuid(person.getPersonUuid());
    }

    //deletePerson
    @Test
    void deletePerson(){
        Person person = Utils.createPersonObj();

        when(this.personsRepository.findByPersonUuid(person.getPersonUuid()))
                .thenReturn(Optional.of(person));

        personsService.deletePerson(person.getPersonUuid());

        verify(personsRepository).findByPersonUuid(person.getPersonUuid());
        verify(personsRepository).delete(person);
    }

    @Test
    void deletePerson_NotFound(){
        Person person = Utils.createPersonObj();

        when(this.personsRepository.findByPersonUuid(person.getPersonUuid()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> personsService.deletePerson(person.getPersonUuid()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(personsRepository).findByPersonUuid(person.getPersonUuid());
        verify(personsRepository, never()).save(any(Person.class));
    }

}
