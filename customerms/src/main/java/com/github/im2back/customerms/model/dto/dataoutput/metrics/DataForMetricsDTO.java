package com.github.im2back.customerms.model.dto.dataoutput.metrics;

import java.util.List;

public record DataForMetricsDTO(
		Double totalValueForLastMonth,
		Double partialValueForCurrentMonth,
		Double partialValueForCurrentDay,
		Double totalOutstandingAmount,
		List<DailyTotalDTO> dataGraphicSevenDays 
		) {

}
