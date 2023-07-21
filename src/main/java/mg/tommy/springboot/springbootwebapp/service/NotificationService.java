package mg.tommy.springboot.springbootwebapp.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import mg.tommy.springboot.springbootwebapp.model.domain.embedded.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Author author) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("noreply+tommy@gmail.com");
        mail.setTo(author.getEmail());
        mail.setSubject("Spring Boot registration success");
        mail.setText("Hello, your registration to our site has succeed. This email is just for test purpose. Please, ignore this message if you get it.");
        mailSender.send(mail);
    }

    public void sendFancyEmail(Author author) {
        // Due to Google high restrictions, it seems it's not possible to log in via plain username & password
        // Either we change the setting on the Gmail account to allow access to less secure apps (SMTP client not from Google)
        // or we have to generate a Gmail App Password (and activate 2-step verification) and forward that instead of the password (not tested yet)
        MimeMessagePreparator preparator = (MimeMessage mimeMessage) -> {
            mimeMessage.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(author.getEmail()));
            mimeMessage.setFrom(new InternetAddress("rabesalama.tommy@gmail.com"));
            mimeMessage.setSubject("Spring Boot registration success");
            mimeMessage.setText("Hello, your registration to our site has succeed. This email is just for test purpose. Please, ignore this message if you get it.");
        };
        
        mailSender.send(preparator);
    }
}
