package com.example.currency.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.currency.request.BillRequest;
import com.example.currency.response.BillResponse;
import com.example.currency.service.CurrencyExchangeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CurrencyExchangeController {

	private static final Logger logger = LogManager.getLogger(CurrencyExchangeController.class);

	private final CurrencyExchangeService currencyExchangeService;

	public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService) {
		this.currencyExchangeService = currencyExchangeService;
	}

	/**
	 * This methods is used to calculate discount and return amount in exchanged
	 * currency.
	 * 
	 * @param requestBody
	 * @return target currency with discounted Amount
	 */
	@PostMapping("/calculate")
	public ResponseEntity<BillResponse> currencyExchange(@Valid @RequestBody BillRequest request) {
		logger.info("Initiate calculateBill request for user Type: {}", request.getUserType());
		BillResponse billResponse = currencyExchangeService.exchangeCurrencyAndCalculateDiscount(request);
		logger.info("Completed calculateBill method for user Type: {}", request.getUserType());
		return ResponseEntity.ok(billResponse);
	}
}