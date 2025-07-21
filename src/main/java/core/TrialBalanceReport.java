package core;

import com.google.common.base.MoreObjects;
import core.account.AccountDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a trial balance with debit and credit totals.
 */
public class TrialBalanceReport {
    private final Map<AccountDetails, BigDecimal> debitAmounts =
            new TreeMap<>(Comparator.comparing(AccountDetails::getAccountNumber));
    private final Map<AccountDetails, BigDecimal> creditAmounts =
            new TreeMap<>(Comparator.comparing(AccountDetails::getAccountNumber));
    private final BigDecimal totalDebit;
    private final BigDecimal totalCredit;
    private final boolean balanced;

    public TrialBalanceReport(Map<AccountDetails, BigDecimal> balances) {
        checkNotNull(balances);
        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        for (var entry : balances.entrySet()) {
            BigDecimal amount = entry.getValue();
            if (amount.signum() >= 0) {
                debitAmounts.put(entry.getKey(), amount);
                debit = debit.add(amount);
            } else {
                BigDecimal abs = amount.abs();
                creditAmounts.put(entry.getKey(), abs);
                credit = credit.add(abs);
            }
        }
        totalDebit = debit;
        totalCredit = credit;
        balanced = totalDebit.compareTo(totalCredit) == 0;
    }

    public Map<AccountDetails, BigDecimal> getDebitAmounts() {
        return new TreeMap<>(debitAmounts);
    }

    public Map<AccountDetails, BigDecimal> getCreditAmounts() {
        return new TreeMap<>(creditAmounts);
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public boolean isBalanced() {
        return balanced;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("debitAmounts", debitAmounts)
                .add("creditAmounts", creditAmounts)
                .add("totalDebit", totalDebit)
                .add("totalCredit", totalCredit)
                .add("balanced", balanced)
                .toString();
    }
}
