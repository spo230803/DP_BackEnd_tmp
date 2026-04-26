package at.spengergasse.dp_backend.service.system;

import at.spengergasse.dp_backend.GlobalConstat;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{
    @Autowired
    private JavaMailSender mailSender;

    private GlobalConstat globalConstat = new GlobalConstat();

    /**
     * Funzione usata per inviare Email in testo Semplice
     * @param to - Emal del Destinatario
     * @param subject - Oggetto della Email
     * @param text - Corpo delle Emal in formato Testo semplice
     */
    public void sendEmailSiple(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(globalConstat.getEmailSend()); // opzionale, meglio metterlo
        //noreplay_AF-Ticketconnect@vbgio.net

        mailSender.send(message);
    }

    /**
     * Funzone per inviare Email in formato HTML
     * Nota : i Link contentuti nelle Email possono essere consierati come sapm!
     * @param to
     * @param subject -
     * @param htmlContent - Corpo della Email infomrato HTML
     * @throws MessagingException
     */
    public void sendEmailHtml(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML
        helper.setFrom(globalConstat.getEmailSend());

        mailSender.send(message);
    }
}//end class
