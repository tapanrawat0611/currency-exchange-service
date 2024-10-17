package com.example.currency.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BillResponse {

	private BigDecimal payableAmount;
	private String currency;

	public BillResponse(BigDecimal payableAmount, String currency) {
		this.payableAmount = payableAmount;
		this.currency = currency;
	}
}
