package com.github.im2back.orchestrator.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardError {

	private Integer status;
	private String error;
	private List<String> messages;
	private String path;
}
