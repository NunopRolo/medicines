package com.nr.medicines.repositories;

import com.nr.medicines.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonsRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByPersonUuid(UUID personUuid);
}
