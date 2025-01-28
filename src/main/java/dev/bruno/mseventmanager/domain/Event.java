package dev.bruno.mseventmanager.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "db_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event implements Serializable {
    @Id
    @Column(unique = true, nullable = false)
    @JsonProperty("eventId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    public Event(String eventName, String date, String cep, String logradouro, String bairro, String cidade, String uf) {
        this.eventName = eventName;
        this.date = date;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }
}
