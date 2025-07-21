import assistant.Assistant;
import core.Ledger;
import core.chartofaccounts.ChartOfAccounts;
import core.chartofaccounts.ChartOfAccountsBuilder;
import core.transaction.AccountingTransaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssistantTest {
    @Test
    public void testHandleTrialBalance() {
        String cashAccountNumber = "000001";
        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", CREDIT)
                .build();
        Ledger ledger = new Ledger(coa);
        AccountingTransaction t = ledger.createTransaction(null)
                .debit(new BigDecimal(10), cashAccountNumber)
                .credit(new BigDecimal(10), cashAccountNumber)
                .build();
        ledger.commitTransaction(t);

        Assistant assistant = new Assistant();
        String response = assistant.handle("trial balance", ledger);
        assertTrue(response.contains("accountNumberToAccountMap"));
    }
}
