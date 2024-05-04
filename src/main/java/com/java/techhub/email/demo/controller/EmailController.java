/**
 * 
 */
package com.java.techhub.email.demo.controller;

import com.java.techhub.email.demo.model.*;
import com.java.techhub.email.demo.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import java.util.*;

/**
 * @author mahes
 *
 */
@RestController
@RequestMapping("/email")
public class EmailController {
	
	@Autowired
	private EmailService emailService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody MaileUser user) throws MessagingException{
		Map<String, Object> map = new HashMap<>();
		map.put("message", emailService.sendEmail(user));
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
