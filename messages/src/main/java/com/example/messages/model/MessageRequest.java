package com.example.messages.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
	
	@NotBlank(message = "Email body is required")
	private String emailBody;
	
	@NotBlank(message = "First name is required")
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	private String lastName;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;
	
	@NotBlank(message = "Subject is required")
	private String subject;

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "MessageRequest [emailBody=" + emailBody + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", subject=" + subject + "]";
	}
	
	

}
