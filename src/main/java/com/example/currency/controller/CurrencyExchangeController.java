package com.example.currency.controller;

import java.math.BigDecimal;

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
import com.example.currency.service.DiscountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CurrencyExchangeController {

	private static final Logger logger = LogManager.getLogger(CurrencyExchangeController.class);

	private final CurrencyExchangeService currencyExchangeService;
	private final DiscountService discountService;

	public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService,
			DiscountService discountService) {
		this.currencyExchangeService = currencyExchangeService;
		this.discountService = discountService;
	}

	/**
	 * This methods is used to calculate discount and return amount in exchanged
	 * currency.
	 * 
	 * @param requestBody
	 * @return target currency with discounted Amount
	 */
	@PostMapping("/calculate")
	public ResponseEntity<?> currencyExchange(@Valid @RequestBody BillRequest request) {
		logger.info("Initiate calculateBill request for user Type: {}", request.getUserType());
		BigDecimal totalDiscount = discountService.calculateDiscount(request.getAmount(), request.getUserType(),
				request.isGrocery(), request.getCustomerTenure());

		BigDecimal discountedAmount = request.getAmount().subtract(totalDiscount);
		BigDecimal exchangeRate = currencyExchangeService.getExchangeRate(request.getOriginalCurrency(),
				request.getTargetCurrency());

		BigDecimal payableAmount = discountedAmount.multiply(exchangeRate);
		logger.info("Completed calculateBill method for user Type: {}", request.getUserType());
		return ResponseEntity.ok(new BillResponse(payableAmount, request.getTargetCurrency()));
	}
}