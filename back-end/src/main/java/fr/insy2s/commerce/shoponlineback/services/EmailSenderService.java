package fr.insy2s.commerce.shoponlineback.services;

import fr.insy2s.commerce.shoponlineback.exceptions.advices.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;

	public void sendSimpleEmail(String to, String subject, String body, String template) {
		MimeMessagePreparator messageToSend = mimeMessage -> {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setFrom("buyoyaneymar@gmail.com");
			message.setTo(to);
			message.setText(mailContentBuilder.htmlBuilderContent(body, template), true);
			message.setSubject(subject);
		};
		try {
			mailSender.send(messageToSend);
		} catch (MailException e) {
			new ErrorResponse("erreur d'envoie de mail",e.toString());
		}
	}

}