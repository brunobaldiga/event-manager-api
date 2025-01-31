package dev.bruno.msticketmanager.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"eventId", "eventName", "eventDateTime", "logradouro", "bairro", "cidade", "uf"})
public class Event {
    private String eventId;
    private String eventName;

    @JsonAlias("date")
    @JsonProperty("eventDateTime")
    private String date;

    @JsonIgnore
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}
