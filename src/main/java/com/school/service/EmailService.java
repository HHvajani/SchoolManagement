package com.school.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public boolean sendEmail(String subject, String message, String to) {
		// Variable for gmail host
		
		boolean f = false;

		String from = "TypeYourEmail..here";

		String host = "smtp.gmail.com";

		// get the system properties

		Properties properties = System.getProperties();
		System.out.println("Properties : " + properties);

		// setting imp information to properties object

		// host setting

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Step 1 : to get the session object.....

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("TypeYourEmail..here", "");
			}
		});
		session.setDebug(true);

		// step 2 : compose the message [text,multimedia etc u can use.]
		// mimemessage to compose the message

		MimeMessage m = new MimeMessage(session);

		// set some things
		try {

//			// from email
			m.setFrom(from);

			// adding recipient
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject to message
			m.setSubject(subject);

			// adding text to message
//			m.setText(message);
			m.setContent(message,"text/html");

			// send email
			// step 3 : send the message using Transport class

			Transport.send(m);
			System.out.println("Send successfully...!!!");
			f=true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

}
