package dev.bruno.msticketmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Email implements Serializable {
    private String to;
    private String subject;
    private String body;
}
