package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SevenDayGraphData {

	private Date data;
	private BigDecimal valor;

	public SevenDayGraphData(Date dataBruta, BigDecimal valor) {
		this.data = dataBruta;
		this.valor = valor;
	}

}
