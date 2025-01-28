package dev.bruno.msticketmanager.domain.representation;

import dev.bruno.msticketmanager.domain.Ticket;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketSaveRequest {
    private String customerName;

    @Size(min=11, max=11, message = "Insira um CPF válido.")
    private String cpf;

    @Email(message = "Insira um e-mail válido.", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String customerMail;

    private String eventId;
    private String BRLamount;
    private String USDamount;

    public Ticket toModel() {
        return new Ticket(
                cpf, customerName, customerMail,
                BRLamount, USDamount, eventId, "não concluído"
        );
    }
}
