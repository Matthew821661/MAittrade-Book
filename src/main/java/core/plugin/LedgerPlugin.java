package core.plugin;

import core.Ledger;
import core.transaction.AccountingTransaction;

/**
 * Plugin interface for extending Ledger behavior.
 */
public interface LedgerPlugin {
    /**
     * Called after a transaction has been committed to the ledger.
     *
     * @param ledger       the ledger to which the transaction belongs
     * @param transaction  the committed transaction
     */
    void onTransactionCommitted(Ledger ledger, AccountingTransaction transaction);
}
