package core.plugin;

import core.Ledger;
import core.transaction.AccountingTransaction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Simple plugin that assigns a category to a transaction based on keywords in
 * its description field.
 */
public class AutoCategorizationPlugin implements LedgerPlugin {
    private final Map<String, String> keywordCategory = new HashMap<>();

    public AutoCategorizationPlugin() {
        keywordCategory.put("coffee", "Food");
        keywordCategory.put("taxi", "Transport");
        keywordCategory.put("salary", "Income");
    }

    @Override
    public void onTransactionCommitted(Ledger ledger, AccountingTransaction transaction) {
        Map<String, String> info = transaction.getInfo();
        if (info == null) {
            return;
        }
        if (!info.containsKey("category")) {
            String description = info.getOrDefault("description", "").toLowerCase(Locale.ROOT);
            for (Map.Entry<String, String> e : keywordCategory.entrySet()) {
                if (description.contains(e.getKey())) {
                    info.put("category", e.getValue());
                    break;
                }
            }
        }
    }
}
