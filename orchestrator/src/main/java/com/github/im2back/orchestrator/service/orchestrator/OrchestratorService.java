package com.github.im2back.orchestrator.service.orchestrator;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;
import com.github.im2back.orchestrator.service.steps.updatestockstep.UpdateStockStep;
import com.github.im2back.orchestrator.service.steps.validationstep.ValidationStep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrchestratorService {
	
	private final ValidationStep validationStep;
	private final UpdateStockStep updateStockStep;
	private final SaveHistoryStep saveHistoryStep;

	public PurchaseHistoryResponseDTO orchestratePurchase(PurchaseRequestDTO dto) throws JsonProcessingException {

		/** Etapa 1 - Validar compra antes de processar*/
		validationStep.execute(dto);

		/**Etapa 2 - Atualizar estoque e deduzir a quantidade de produtos comprados.*/
		List<StockUpdateResponseDTO> stockUpdateResponseDTOList = updateStockStep.execute(dto);

		/**Etapa 3 - Processar a resposta da Etapa 2, criar um objeto PurchaseHistoryDTO 
		 * e enviá-lo ao serviço customer-ms para persistir o histórico da compra.
		 */
		PurchaseHistoryResponseDTO responseCustomerClient = saveHistoryStep.execute(dto, stockUpdateResponseDTOList);

		return responseCustomerClient;
	}
	
	
}
