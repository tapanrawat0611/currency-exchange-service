package com.example.currency.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.currency.request.BillRequest;
import com.example.currency.response.BillResponse;
import com.example.currency.service.CurrencyExchangeService;
import com.example.currency.service.DiscountService;

class CurrencyExchangeControllerTest {

    @Mock
    private CurrencyExchangeService currencyExchangeService;
	
    @Mock
    private DiscountService discountService;

    @InjectMocks
    private CurrencyExchangeController currencyExchangeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCurrencyExchange_Success() {
        BigDecimal amount = new BigDecimal("1000");
        String userType = "employee";
        String originalCurrency = "USD";
        String targetCurrency = "EUR";
        boolean isGrocery = false;
        LocalDateTime customerTenure = LocalDateTime.now().minusYears(3);
        BillRequest request = new BillRequest(amount, userType, originalCurrency, targetCurrency, isGrocery, customerTenure);
        
        BigDecimal expectedPayableAmount = new BigDecimal("595.00"); 
        BillResponse resp = new BillResponse(expectedPayableAmount, request.getTargetCurrency());

        when(currencyExchangeService.exchangeCurrencyAndCalculateDiscount(request)).thenReturn(resp);
        ResponseEntity<?> response = currencyExchangeController.currencyExchange(request);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof BillResponse);
        
        BillResponse billResponse = (BillResponse) response.getBody();
        assertEquals(expectedPayableAmount, billResponse.getPayableAmount());
        assertEquals(targetCurrency, billResponse.getCurrency());

        verify(currencyExchangeService, times(1)).exchangeCurrencyAndCalculateDiscount(request);
     }
  }

