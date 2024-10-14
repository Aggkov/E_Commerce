package com.ecommerce.core.dto.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ExceptionResponse {
	private String message;
	private HttpStatus status;
	private List<String> messages;
	private Integer code;
	private Instant timestamp;

//	public ExceptionResponse(List<String> messages, String error, HttpStatus status) {
//		setMessages(messages);
//		this.error = error;
//		this.status = status;
//		this.timestamp = Instant.now();
//	}
//
//	public ExceptionResponse(String error, HttpStatus status) {
//		this.error = error;
//		this.status = status;
//		this.timestamp = Instant.now();
//	}

	public List<String> getMessages() {
		return messages == null ? null : new ArrayList<>(messages);
	}

	public final void setMessages(List<String> messages) {

		if (messages == null) {
			this.messages = null;
		} else {
			this.messages = Collections.unmodifiableList(messages);
		}
	}

}
