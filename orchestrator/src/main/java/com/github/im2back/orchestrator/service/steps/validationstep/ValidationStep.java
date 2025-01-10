package com.github.im2back.orchestrator.service.steps.validationstep;

import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;

public interface ValidationStep {

	public void execute(PurchaseRequestDTO dto);
}
