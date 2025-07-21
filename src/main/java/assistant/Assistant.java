package assistant;

import core.Ledger;
import core.TrialBalanceResult;

public class Assistant {
    public String handle(String userInput, Ledger ledger) {
        if (userInput == null || userInput.isBlank()) {
            return "Please enter a command.";
        }
        String normalized = userInput.toLowerCase();
        if (normalized.contains("trial balance")) {
            TrialBalanceResult result = ledger.computeTrialBalance();
            return result.toString();
        } else if (normalized.startsWith("balance")) {
            String[] parts = normalized.split("\\s+");
            if (parts.length == 2) {
                String accountNumber = parts[1];
                return ledger.getAccountBalance(accountNumber).toPlainString();
            }
        }
        return "Sorry, I didn't understand that command.";
    }
}
