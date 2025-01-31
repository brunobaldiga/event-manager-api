package dev.bruno.msticketmanager.domain.representation;

import lombok.*;

@AllArgsConstructor
@Getter @Setter
public class EmailTemplate {

    private final String recipientName;
    private final String eventName;
    private final String eventDate;
    private final String ticketCode;

    public String generateEmailBody() {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0;}" +
                ".email-container {max-width: 600px; margin: 20px auto; background: #ffffff; border-radius: 10px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);}" +
                ".header {background: #4CAF50; color: #ffffff; text-align: center; padding: 20px; border-top-left-radius: 10px; border-top-right-radius: 10px;}" +
                ".header h2 {margin: 0; font-size: 24px;}" +
                ".content {padding: 20px; color: #333; line-height: 1.6;}" +
                ".content p {margin: 10px 0;}" +
                ".content .highlight {color: #4CAF50; font-weight: bold;}" +
                ".footer {text-align: center; font-size: 12px; color: #888; padding: 15px; border-top: 1px solid #ddd;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                "<div class='header'>" +
                "<h2>Confirmação de Compra</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Olá, <span class='highlight'>" + escapeHtml(recipientName) + "</span>,</p>" +
                "<p>Obrigado por adquirir seu ingresso para o evento <span class='highlight'>" + escapeHtml(eventName) + "</span>.</p>" +
                "<p><strong>Data do evento:</strong> " + escapeHtml(eventDate) + "</p>" +
                "<p><strong>Código do ingresso:</strong> <span class='highlight'>" + escapeHtml(ticketCode) + "</span></p>" +
                "<p>Por favor, leve este e-mail no dia do evento e apresente o código para a entrada.</p>" +
                "</div>" +
                "<div class='footer'>Equipe do Evento</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
