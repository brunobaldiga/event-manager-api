package dev.bruno.mseventmanager.repositories;

import dev.bruno.mseventmanager.entities.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
