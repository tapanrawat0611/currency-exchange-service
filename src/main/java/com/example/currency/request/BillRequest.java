package com.example.currency.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BillRequest {

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "1.00", message = "Amount must be greater than zero")
	private BigDecimal amount;

	@NotNull(message = "User type is required")
	@Pattern(regexp = "employee|affiliate|customer", message = "User type must be one of employee, affiliate or customer")
	private String userType;

	@NotNull(message = "Original currency is required")
	@Size(min = 3, max = 3, message = "Original currency must be a valid 3-letter ISO code")
	private String originalCurrency;

	@NotNull(message = "Target currency is required")
	@Size(min = 3, max = 3, message = "Target currency must be a valid 3-letter ISO code")
	private String targetCurrency;

	private boolean isGrocery;
	private LocalDateTime customerTenure;

}
