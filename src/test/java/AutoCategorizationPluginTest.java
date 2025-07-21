import core.Journal;
import core.Ledger;
import core.chartofaccounts.ChartOfAccounts;
import core.chartofaccounts.ChartOfAccountsBuilder;
import core.plugin.AutoCategorizationPlugin;
import core.transaction.AccountingTransaction;
import core.transaction.AccountingTransactionBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutoCategorizationPluginTest {
    @Test
    public void testCategoryAssigned() {
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";
        String arAccountNumber = "000003";

        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", CREDIT)
                .addAccount(checkingAccountNumber, "Checking", CREDIT)
                .addAccount(arAccountNumber, "AR", DEBIT)
                .build();
        Ledger ledger = new Ledger(coa);
        ledger.registerPlugin(new AutoCategorizationPlugin());

        var info = new HashMap<String, String>();
        info.put("description", "Coffee and snack");

        AccountingTransaction t = AccountingTransactionBuilder.create(info)
                .debit(new BigDecimal(10), cashAccountNumber)
                .credit(new BigDecimal(10), arAccountNumber)
                .build();

        ledger.commitTransaction(t);

        assertEquals("Food", t.getInfo().get("category"));
    }
}
