package dev.bruno.mseventmanager.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "db_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    @Column(name="event_name", nullable=false)
    private String eventName;

    @Column(nullable=false)
    private String date;

    @Column(nullable = false)
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;

    public Event(String eventName, String date, String cep) {
        this.eventName = eventName;
        this.date = date;
        this.cep = cep;
    }

    @PrePersist
    private void generateId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
