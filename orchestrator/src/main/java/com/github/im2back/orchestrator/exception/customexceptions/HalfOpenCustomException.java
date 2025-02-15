package com.github.im2back.orchestrator.exception.customexceptions;

import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HalfOpenCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private PurchaseHistoryResponseDTO purchaseHistoryResponseDTO;
	
}
