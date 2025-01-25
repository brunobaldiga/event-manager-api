package dev.bruno.msticketmanager.domain.representation;

import dev.bruno.msticketmanager.domain.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketSaveRequest {
    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String BRLamount;
    private String USDamount;

    public Ticket toModel() {
        return new Ticket(
                cpf, customerName, customerMail,
                BRLamount, USDamount, "não concluído"
        );
    }
}
