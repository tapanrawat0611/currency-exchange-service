package com.example.currency.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void testCalculateDiscount_employee() {
        BigDecimal discount = discountService.calculateDiscount(new BigDecimal("200"), "employee", false, LocalDateTime.now());
        assertEquals(new BigDecimal("70.0"), discount);
    }

    @Test
    void testCalculateDiscount_affiliate() {
        BigDecimal discount = discountService.calculateDiscount(new BigDecimal("100"), "affiliate", false, LocalDateTime.now());
        assertEquals(new BigDecimal("15.0"), discount);
    }

    @Test
    void testCalculateDiscount_customerSince() {
        BigDecimal discount = discountService.calculateDiscount(new BigDecimal("100"), "customer", false, LocalDateTime.now().minusYears(3));
        assertEquals(new BigDecimal("10.00"), discount);
    }
}

