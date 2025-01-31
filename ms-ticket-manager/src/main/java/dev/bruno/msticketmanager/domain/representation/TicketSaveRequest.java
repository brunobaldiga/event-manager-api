package dev.bruno.msticketmanager.domain.representation;

import dev.bruno.msticketmanager.domain.Ticket;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketSaveRequest {
    @NotBlank
    private String customerName;

    @NotBlank
    @Size(min=11, max=11, message = "Insira um CPF válido.")
    private String cpf;

    @NotBlank
    @Email(message = "Insira um e-mail válido.", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String customerMail;

    @NotBlank
    private String eventId;

    @NotBlank
    private String brlAmount;

    @NotBlank
    private String usdAmount;

    public Ticket toModel() {
        return new Ticket(
                cpf, customerName, customerMail,
                brlAmount, usdAmount, eventId, "não concluído"
        );
    }
}
