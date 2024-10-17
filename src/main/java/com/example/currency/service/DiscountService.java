package com.example.currency.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.example.currency.constant.CommonConstants;

@Service
public class DiscountService {

	private static final Logger logger = LogManager.getLogger(DiscountService.class);

	public BigDecimal calculateDiscount(BigDecimal amount, String userType, boolean isGrocery,
			LocalDateTime customerSince) {

		BigDecimal discount = BigDecimal.ZERO;

		if (!isGrocery) {
			if (userType.equals(CommonConstants.EMPLOYEE)) {
				discount = discount.add(amount.multiply(BigDecimal.valueOf(0.3)));
			} else if (userType.equals(CommonConstants.AFFILIATE)) {
				discount = discount.add(amount.multiply(BigDecimal.valueOf(0.1)));
			} else if (customerSince.isBefore(LocalDateTime.now().minusYears(2))) {
				discount = discount.add(amount.multiply(BigDecimal.valueOf(0.05)));
			}
		}

		// Apply additional $5 discount for every $100 spent
		BigDecimal extraDiscount = amount.divideToIntegralValue(BigDecimal.valueOf(100))
				.multiply(BigDecimal.valueOf(5));
		discount = discount.add(extraDiscount);
		logger.info("CalculateDiscount Method: userType: {} has got discount of {} on amount: {}", userType, discount,
				amount);

		return discount;
	}
}
