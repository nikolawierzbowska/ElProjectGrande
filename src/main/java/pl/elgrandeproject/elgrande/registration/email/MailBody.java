package pl.elgrandeproject.elgrande.registration.email;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {

}
