package com.github.im2back.customerms.model.dto.dataoutput;

import java.util.List;

public record DataForMetricsDto(
		Double totalValueForLastMonth,
		Double partialValueForCurrentMonth,
		Double partialValueForCurrentDay,
		Double totalOutstandingAmount,
		List<DailyTotal> dataGraphicSevenDays 
		
		) {

}
