package com.example.currency.response;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class ExchangeRateResponse {

	private String base;
	private Map<String, BigDecimal> rates;

}
