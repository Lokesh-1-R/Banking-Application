package com.axis.account;

import java.math.BigDecimal;

public record AccountOverviewResponse( BigDecimal accountBalance,
        String accountNumber,
        String AccountType,
        String accountStatus
		) {

}
