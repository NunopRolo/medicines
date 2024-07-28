package com.nr.medicines.services;

import com.nr.medicines.models.Person;
import com.nr.medicines.models.dto.PersonRequest;
import com.nr.medicines.repositories.PersonsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonsService {

    private final PersonsRepository personsRepository;

    public PersonsService(
            PersonsRepository personsRepository
    ){
        this.personsRepository = personsRepository;
    }

    public Person createNewPerson(PersonRequest personRequest){
        Person person = new Person();
        person.setName(personRequest.getName());

        return personsRepository.save(person);
    }

    public List<Person> getAllPersons(){
        return personsRepository.findAll();
    }

    public Person getPersonByUuid(UUID personUuid){
        Optional<Person> personOptional = this.personsRepository.findByPersonUuid(personUuid);
        if(personOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }

        return personOptional.get();
    }

    public void deletePerson(UUID personUuid){
        Optional<Person> personOptional = this.personsRepository.findByPersonUuid(personUuid);
        if(personOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found");
        }

        personsRepository.delete(personOptional.get());
    }


}
