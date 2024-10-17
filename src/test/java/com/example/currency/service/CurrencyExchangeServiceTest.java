package com.example.currency.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.currency.exceptionhandler.CustomException;
import com.example.currency.response.ExchangeRateResponse;


@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceTest {

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String baseCurrency = "USD";
    private final String targetCurrency = "EUR";

    
    @Test
    void testGetExchangeRate_Success() {
        
        BigDecimal expectedRate = new BigDecimal("0.920022");
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(targetCurrency, expectedRate);
        mockResponse.setRates(rates);

        ResponseEntity<ExchangeRateResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class))).thenReturn(responseEntity);

        BigDecimal actualRate = currencyExchangeService.getExchangeRate(baseCurrency, targetCurrency);

        assertEquals(expectedRate, actualRate);
    }

    @Test
    void testGetExchangeRate_CurrencyNotFound() {

        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setRates(Collections.emptyMap()); // No rates returned

        ResponseEntity<ExchangeRateResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class))).thenReturn(responseEntity);

        CustomException exception = assertThrows(CustomException.class, () -> {
            currencyExchangeService.getExchangeRate(baseCurrency, targetCurrency);
        });
        assertEquals("Failed to fetch exchange rates due to an unexpected error.", exception.getMessage());
    }

    @Test
    void testGetExchangeRate_InvalidResponse() {

        ResponseEntity<ExchangeRateResponse> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class))).thenReturn(responseEntity);

        CustomException exception = assertThrows(CustomException.class, () -> {
            currencyExchangeService.getExchangeRate(baseCurrency, targetCurrency);
        });
        assertEquals("Failed to fetch exchange rates due to an unexpected error.", exception.getMessage());
    }

    @Test
    void testGetExchangeRate_ClientError() {

        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        CustomException exception = assertThrows(CustomException.class, () -> {
            currencyExchangeService.getExchangeRate(baseCurrency, targetCurrency);
        });
        assertEquals("Failed to fetch exchange rates due to client error.", exception.getMessage());
    }

    @Test
    void testGetExchangeRate_NetworkError() {

        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class)))
                .thenThrow(new RestClientException("Network error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            currencyExchangeService.getExchangeRate(baseCurrency, targetCurrency);
        });
        assertEquals("Failed to fetch exchange rates due to network error.", exception.getMessage());
    }

    @Test
    void testGetExchangeRate_UnexpectedError() {

        when(restTemplate.getForEntity(anyString(), eq(ExchangeRateResponse.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            currencyExchangeService.getExchangeRate(baseCurrency, targetCurrency);
        });
        assertEquals("Failed to fetch exchange rates due to an unexpected error.", exception.getMessage());
    }
}
