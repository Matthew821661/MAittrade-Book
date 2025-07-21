import core.Ledger;
import core.TrialBalanceResult;
import core.chartofaccounts.ChartOfAccounts;
import core.chartofaccounts.ChartOfAccountsBuilder;
import core.transaction.AccountingTransaction;
import core.plugin.AutoCategorizationPlugin;
import java.util.HashMap;

import java.math.BigDecimal;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;

public class Main {
    public static void main(String[] args) {
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";
        String accountsReceivableAccountNumber = "000003";

        // Setup ledger
        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", CREDIT)
                .addAccount(checkingAccountNumber, "Checking", CREDIT)
                .addAccount(accountsReceivableAccountNumber, "Accounts Receivable", DEBIT)
                .build();
        Ledger ledger = new Ledger(coa);
        // Register a simple categorization plugin
        ledger.registerPlugin(new AutoCategorizationPlugin());

        // Accounts Receivable 35 was settled with cash 10 and wire transfer 25
        var info = new HashMap<String, String>();
        info.put("description", "Coffee purchase");

        AccountingTransaction t = ledger.createTransaction(info)
                .debit(new BigDecimal(10), cashAccountNumber)
                .debit(new BigDecimal(25), checkingAccountNumber)
                .credit(new BigDecimal(35), accountsReceivableAccountNumber)
                .build();
        ledger.commitTransaction(t);

        // Print assigned category
        System.out.println("Category: " + t.getInfo().get("category"));

        // Print ledger
        System.out.println(ledger.toString());

        // Print trial balance
        TrialBalanceResult trialBalanceResult = ledger.computeTrialBalance();
        System.out.println(trialBalanceResult.toString());
    }
}
