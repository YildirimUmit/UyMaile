/**
 * 
 */
package com.java.techhub.email.demo.service;

import com.java.techhub.email.demo.enums.*;
import com.java.techhub.email.demo.model.*;
import lombok.extern.slf4j.*;
import org.apache.tomcat.util.buf.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.*;
import org.thymeleaf.spring5.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author mahes
 *
 */
@Slf4j
@Service
public class SimpleEmailService implements EmailService {

	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${user.personal.repo.link}")
	private String userLink;

	@Value("${user.personal.repo.type}")
	private String userBlogType;

	@Override
	public ArrayList<MaileSendInfo>  sendEmail(MaileUser user) throws MessagingException {
		ArrayList<MaileSendInfo> maileSendInfos=new ArrayList<MaileSendInfo>();
		log.info("Sending emails... message " );
		final AtomicInteger counter = new AtomicInteger(0);
		user.getEmails().forEach(usr -> {
			try {
				log.info("Sending message "+usr.toString());
				Context context = new Context();
				Map<String, Object> map = new HashMap<>();
				map.put("firstname", usr.getFirstName());
				map.put("lastname", usr.getLastName());
				map.put("name", StringUtils.join(Arrays.asList(usr.getFirstName(), usr.getLastName()), ' '));
//				map.put("sign", StringUtils.join(Arrays.asList(user.getFirstName(), user.getLastName()), ' '));
//				map.put("location", user.getLocation());
				map.put("uniqueid", UUID.randomUUID().toString());
				map.put("repo", userLink);
				map.put("blogtype", userBlogType);
				map.put("url", usr.getMessage());
				context.setVariables(map);
				String process = springTemplateEngine.process("welcome", context);
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				String subject = StringUtils
						.join(Arrays.asList("Greetings", usr.getFirstName(), usr.getLastName(), "!!!"), ' ');
				helper.setSubject(subject);
				helper.setText(process, true);
				helper.setTo(usr.getEmail());
				helper.setFrom(username);
				javaMailSender.send(mimeMessage);
				MaileSendInfo maileSendInfo=new MaileSendInfo();
				maileSendInfo.setSendError(false);
				maileSendInfo.setMaileAddress(usr.getEmail());
				maileSendInfo.setSendInfo(MaileSendStatus.SUCCESS.getStatus());
				maileSendInfo.setMaileAddress("Email(s) sent successfully, Please check your inbox...!!!");
				maileSendInfos.add(maileSendInfo);
			} catch (MessagingException | MailException ex) {
				MaileSendInfo maileSendInfo=new MaileSendInfo();
				maileSendInfo.setSendError(false);
				maileSendInfo.setMaileAddress(usr.getEmail());
				maileSendInfo.setSendInfo(MaileSendStatus.ERROR.getStatus());
				maileSendInfo.setMaileAddress("Exception occured while sending email to:   "+ usr.getFirstName()+usr.getLastName()+", due to:" +ex.getMessage());
				maileSendInfos.add(maileSendInfo);
				log.error("Exception occured while sending email to: {} {}, due to: {}", usr.getFirstName(),
						usr.getLastName(), ex.getMessage());
				counter.incrementAndGet();
			}
		});
//		if (counter.intValue() > 0) {
//			return counter.intValue() + " email(s) sending failed. Please verify logs...!!!";
//		}


		return  maileSendInfos;
	}
}
