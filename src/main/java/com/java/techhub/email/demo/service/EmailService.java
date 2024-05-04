/**
 * 
 */
package com.java.techhub.email.demo.service;

import com.java.techhub.email.demo.model.*;

import javax.mail.*;
import java.util.*;

/**
 * @author mahes
 *
 */
public interface EmailService {

	ArrayList<MaileSendInfo> sendEmail(MaileUser user) throws MessagingException;
}
