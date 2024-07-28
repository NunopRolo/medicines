package com.nr.medicines.controllers;

import com.nr.medicines.models.Person;
import com.nr.medicines.models.dto.PersonRequest;
import com.nr.medicines.services.PersonsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
    private final PersonsService personsService;

    public PersonController(PersonsService personsService){
        this.personsService = personsService;
    }

    @GetMapping(value = {"/all"})
    public List<Person> getAllPersons(){
        return personsService.getAllPersons();
    }

    @PostMapping(value = {"/add"})
    public Person addPerson(@RequestBody PersonRequest personRequest){
        return this.personsService.createNewPerson(personRequest);
    }

    @DeleteMapping(value = {"/{personUuid}"})
    public void deletePerson(@PathVariable String personUuid){
        this.personsService.deletePerson(UUID.fromString(personUuid));
    }

}
