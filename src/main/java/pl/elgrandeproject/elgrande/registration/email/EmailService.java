package pl.elgrandeproject.elgrande.registration.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
        simpleMailMessage.setTo(mailBody.to());
        simpleMailMessage.setFrom("");
        simpleMailMessage.setSubject(mailBody.subject());
        simpleMailMessage.setText(mailBody.text());

        javaMailSender.send(simpleMailMessage);

    }
}
