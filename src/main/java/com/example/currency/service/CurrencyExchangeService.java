package com.example.currency.service;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.currency.constant.CommonConstants;
import com.example.currency.exceptionhandler.CustomException;
import com.example.currency.request.BillRequest;
import com.example.currency.response.BillResponse;
import com.example.currency.response.ExchangeRateResponse;

@Service
public class CurrencyExchangeService {

	private static final Logger logger = LogManager.getLogger(CurrencyExchangeService.class);

	@Value("${api.key}")
	private String apiKey;

	@Value("${api.url}")
	private String apiUrl;

	@Autowired
	private RestTemplate restTemplate;

	private final DiscountService discountService;

	public CurrencyExchangeService(DiscountService discountService) {
		this.discountService = discountService;
	}

	public BillResponse exchangeCurrencyAndCalculateDiscount(BillRequest request) {
		BigDecimal exchangeRate = getExchangeRate(request.getOriginalCurrency(), request.getTargetCurrency());

		BigDecimal totalDiscount = discountService.calculateDiscount(request.getAmount(), request.getUserType(),
				request.isGrocery(), request.getCustomerTenure());

		BigDecimal discountedAmount = request.getAmount().subtract(totalDiscount);
		BigDecimal payableAmount = discountedAmount.multiply(exchangeRate);

		return new BillResponse(payableAmount, request.getTargetCurrency());
	}

	public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) {

		try {
			String url = String.format("%s%s%s%s", apiUrl, baseCurrency, CommonConstants.API_KEY, apiKey);
			logger.info("Initiate get exchange rate request url: {} for target currency: {}", url, targetCurrency);

			ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				ExchangeRateResponse exchangeRateResponse = response.getBody();

				if (exchangeRateResponse.getRates() != null
						&& exchangeRateResponse.getRates().containsKey(targetCurrency)) {
					return exchangeRateResponse.getRates().get(targetCurrency);
				} else {
					logger.error("Exchange rate for target currency {} not found.", targetCurrency);
					throw new CustomException("Exchange rate for target currency not found.");
				}
			} else {
				logger.error("Invalid response from exchange rate API: Status Code {}", response.getStatusCode());
				throw new CustomException("Failed to fetch exchange rates for request:" + url);
			}

		} catch (HttpClientErrorException e) {
			logger.error("Client error while fetching exchange rate: {}", e.getMessage());
			throw new CustomException("Failed to fetch exchange rates due to client error.");
		} catch (RestClientException e) {
			logger.error("Error occurred while fetching exchange rate: {}", e.getMessage());
			throw new CustomException("Failed to fetch exchange rates due to network error.");
		} catch (Exception e) {
			logger.error("Unexpected error occurred while fetching exchange rate: {}", e.getMessage());
			throw new CustomException("Failed to fetch exchange rates due to an unexpected error.");
		}
	}
}
