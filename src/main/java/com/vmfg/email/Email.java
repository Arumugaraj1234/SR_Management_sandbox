package com.vmfg.email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageTemplateEntity;


public class Email {
	private static final Logger logger = LoggerFactory.getLogger(Email.class);
	
	public boolean sendMail(MessageLogEntity messageLog, MessageTemplateEntity messageTemplate) {
		try {
			logger.info("messageLog.getMsgSubject()-------> "+messageLog.getMsgSubject());
			Session session = getSession(messageTemplate);
			Message mailMessage = new MimeMessage(session);
			mailMessage.setFrom(new InternetAddress(messageTemplate.getMsgFromUsername()));
			String contents = new String(Files.readAllBytes(Paths.get(messageLog.getSendMsgFilePath())));
			logger.info("messageLog.getMsgSubject()-------> "+messageLog.getMsgSubject());
			// Set the mail recipients
			String msgCC= "";
			if(messageTemplate.getMsgCc() != null) {
				if(!messageTemplate.getMsgCc().equalsIgnoreCase("")) {
					msgCC = messageTemplate.getMsgCc();	
				}else {
					msgCC = messageTemplate.getMsgCc();
				}
			}else {
				msgCC = messageTemplate.getMsgCc();
			}
			setRecipients(mailMessage, messageLog.getMsgTo(), msgCC , messageLog.getMsgCc());
			
			mailMessage.setHeader("X-Priority", messageTemplate.getMsgPriority());
			mailMessage.setSubject(messageLog.getMsgSubject());
			mailMessage.setSentDate(new Date());
			mailMessage.setContent(contents, "text/html");
			mailMessage.saveChanges();
			Transport.send(mailMessage);			
			
		} catch (IOException e) {
			logger.error("sendMail Catch 1 -->" + e);
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			logger.error("sendMail Catch 2 -->" + e);
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			logger.error("sendMail Catch 3 -->" + e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Session getSession(MessageTemplateEntity messageTemplate) {
		Session session = null;
		logger.info("sendMail getSession   method Start");
		try {
			final String from = messageTemplate.getMsgFromUsername();
			final String password = messageTemplate.getMsgFromPassword();
			final String hostName = messageTemplate.getMsgFromHost();
			//final String hostName = "localhost";
			final String portName = messageTemplate.getMsgFromPort();
			
			
			Properties props = new Properties();
			
			
			//props.put("mail.smtp.host", "sg2plcpnl0093.prod.sin2.secureserver.net");
			props.put("mail.smtp.host", hostName);
			//props.put("mail.smtp.socketFactory.port", "465");
//			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.ssl.enable", "true");   // <-- REQUIRED for Gmail 587
//			props.put("mail.smtp.ssl.required", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.auth", "true");
			//props.put("mail.smtp.port", "465");
			props.put("mail.smtp.port", portName);
			// get Session
			session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, password);
				}
			});

		} catch (Exception ex) {
			logger.error("sendMail getSession   method Exception: " + ex);
		}
		logger.info("sendMail getSession   method end");
		return session;
	}
	
	private void setRecipients(Message mailMessage, String to, String cc, String bcc) throws Exception {
		logger.info("sendMail setRecipients   method Start");
		if (mailMessage != null) {
			try {
				// set CC recipients
				if (to != null) {
					List<InternetAddress> toList = new ArrayList<>();
					StringTokenizer tokenizer = new StringTokenizer(to, ",");
					while (tokenizer.hasMoreTokens()) {
						toList.add(new InternetAddress(tokenizer.nextToken()));
					}
					mailMessage.setRecipients(Message.RecipientType.TO,
							(InternetAddress[]) toList.toArray(new InternetAddress[0]));
				}
				// set CC recipients
				if (cc != null) {
					List<InternetAddress> ccList = new ArrayList<>();
					StringTokenizer tokenizer = new StringTokenizer(cc, ",");
					while (tokenizer.hasMoreTokens()) {
						ccList.add(new InternetAddress(tokenizer.nextToken()));
					}
					mailMessage.setRecipients(Message.RecipientType.CC,
							(InternetAddress[]) ccList.toArray(new InternetAddress[0]));
				}

				// set BCC recipients
				if (bcc != null) {
					List<InternetAddress> bccList = new ArrayList<>();
					StringTokenizer tokenizer = new StringTokenizer(bcc, ",");
					while (tokenizer.hasMoreTokens()) {
						bccList.add(new InternetAddress(tokenizer.nextToken()));
					}
					mailMessage.setRecipients(Message.RecipientType.BCC,
							(InternetAddress[]) bccList.toArray(new InternetAddress[0]));
				}
			} catch (MessagingException e) {
				logger.error("sendMail setRecipients method Exception: " + e);
			} catch (Exception e) {
				logger.error("sendMail setRecipients method Exception: " + e);
			}
		}
		logger.info("sendMail setRecipients   method end");
	}
	
}
