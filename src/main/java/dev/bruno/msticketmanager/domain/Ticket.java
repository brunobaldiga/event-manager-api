package dev.bruno.msticketmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Entity
@Document(collection = "db_ticket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonPropertyOrder({"ticketId", "cpf", "customerName", "customerMail", "event", "BRLtotalAmount", "USDtotalAmount", "status"})
public class Ticket {
    @Id
    @Column(unique = true, nullable = false)
    @JsonProperty("ticketId")
    private String id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerMail;

    @Column(nullable = false)
    @JsonProperty("BRLtotalAmount")
    private String BRLamount;

    @JsonProperty("USDtotalAmount")
    @Column(nullable = false)
    private String USDamount;

    @Column(nullable = false)
    private String status;

    @Transient
    private Event event;

    @Column
    @JsonIgnore
    private Boolean active = true;

    public Ticket(String cpf, String customerName, String customerMail, String BRLamount, String USDamount, String status) {
        this.cpf = cpf;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.BRLamount = BRLamount;
        this.USDamount = USDamount;
        this.status = status;
    }

    @PrePersist
    private void generateId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}