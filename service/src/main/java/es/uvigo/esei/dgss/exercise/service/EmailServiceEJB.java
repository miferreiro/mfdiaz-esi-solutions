package es.uvigo.esei.dgss.exercise.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uvigo.esei.dgss.exercises.domain.User;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EmailServiceEJB {

	@Resource(name = "java:jboss/mail/gmail")
    private Session session;
	
	public void sendEmail(User u, String subject, String body) {
        try {

            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getLogin() + "@gmail.com"));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

        } catch (MessagingException e) {
            Logger.getLogger(EmailServiceEJB.class.getName()).log(Level.WARNING, "Cannot send mail", e);
        }
	}
}
