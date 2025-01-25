package dev.bruno.mseventmanager.repositories;

import dev.bruno.mseventmanager.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface EventRepository extends MongoRepository<Event, String> {
    Page<Event> findAll(Pageable pageable);
}
