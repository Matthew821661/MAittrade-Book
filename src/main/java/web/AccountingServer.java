package web;

import core.Ledger;
import core.chartofaccounts.ChartOfAccounts;
import core.chartofaccounts.ChartOfAccountsBuilder;
import core.transaction.AccountingTransaction;
import core.account.AccountSide;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

/**
 * Minimal HTTP server exposing ledger operations.
 * This is an example skeleton to start building a SaaS layer
 * around the in-memory bookkeeping library.
 */
public class AccountingServer {
    private static final Map<String, Ledger> ledgers = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        port(4567);

        post("/create-ledger", (req, res) -> {
            ChartOfAccounts coa = ChartOfAccountsBuilder.create().build();
            Ledger ledger = new Ledger(coa);
            String id = "ledger-" + System.currentTimeMillis();
            ledgers.put(id, ledger);
            res.type("application/json");
            return "{\"ledgerId\":\"" + id + "\"}";
        });

        post("/add-account/:ledgerId", (req, res) -> {
            Ledger ledger = ledgers.get(req.params("ledgerId"));
            if (ledger == null) {
                res.status(404);
                return "Ledger not found";
            }
            String number = req.queryParams("number");
            String name = req.queryParams("name");
            AccountSide side = AccountSide.valueOf(req.queryParams("side"));
            ChartOfAccounts coa = ledger.getChartOfAccounts();
            coa = ChartOfAccountsBuilder.from(coa)
                    .addAccount(number, name, side)
                    .build();
            ledgers.put(req.params("ledgerId"), new Ledger(coa, ledger.getJournal()));
            return "Account added";
        });

        post("/post-transaction/:ledgerId", (req, res) -> {
            Ledger ledger = ledgers.get(req.params("ledgerId"));
            if (ledger == null) {
                res.status(404);
                return "Ledger not found";
            }
            String debitAcc = req.queryParams("debitAcc");
            BigDecimal debitAmount = new BigDecimal(req.queryParams("debit"));
            String creditAcc = req.queryParams("creditAcc");
            BigDecimal creditAmount = new BigDecimal(req.queryParams("credit"));
            AccountingTransaction t = ledger.createTransaction(null)
                    .debit(debitAmount, debitAcc)
                    .credit(creditAmount, creditAcc)
                    .build();
            ledger.commitTransaction(t);
            return "Transaction posted";
        });
    }
}
