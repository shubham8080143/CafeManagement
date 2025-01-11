package com.project.CafeManagement.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender emailSender;

	/**
	 * Sends a simple email message.
	 * 
	 * @param to      - Recipient's email address
	 * @param subject - Subject of the email
	 * @param body    - Body content of the email
	 */
	public void sendSimpleMessage(String to, String subject, String body, List<String> ccList) {
		// Create a SimpleMailMessage object
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("Shubham Thorat");

		// Set recipient, subject, and content
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		if (ccList != null && ccList.size() > 0)
			message.setCc(getCcArray(ccList));
		emailSender.send(message);

	}

	private String[] getCcArray(List<String> ccList) {
		String[] cc = new String[ccList.size()];
		for (int i = 0; i < ccList.size(); i++) {
			cc[i] = ccList.get(i);
		}
		return cc;
	}

	// Send the email

}
