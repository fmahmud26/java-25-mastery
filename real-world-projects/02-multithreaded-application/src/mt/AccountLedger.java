package mt;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Bank-like ledger: per-account ReentrantLock to avoid deadlock via ordered lock acquisition.
 */
public final class AccountLedger {
    private final ConcurrentHashMap<String, BigDecimal> balances = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public AccountLedger seed(String accountId, BigDecimal balance) {
        balances.put(accountId, balance);
        locks.put(accountId, new ReentrantLock());
        return this;
    }

    public BigDecimal balance(String accountId) {
        return balances.getOrDefault(accountId, BigDecimal.ZERO);
    }

    public boolean transfer(String from, String to, BigDecimal amount) {
        if (amount.signum() <= 0) {
            return false;
        }
        ReentrantLock first;
        ReentrantLock second;
        // Deterministic lock order prevents deadlock between A↔B transfers.
        if (from.compareTo(to) < 0) {
            first = lockFor(from);
            second = lockFor(to);
        } else {
            first = lockFor(to);
            second = lockFor(from);
        }

        first.lock();
        try {
            second.lock();
            try {
                BigDecimal fromBal = balances.getOrDefault(from, BigDecimal.ZERO);
                if (fromBal.compareTo(amount) < 0) {
                    return false;
                }
                balances.put(from, fromBal.subtract(amount));
                balances.merge(to, amount, BigDecimal::add);
                return true;
            } finally {
                second.unlock();
            }
        } finally {
            first.unlock();
        }
    }

    private ReentrantLock lockFor(String accountId) {
        return locks.computeIfAbsent(accountId, _ -> new ReentrantLock());
    }

    public Map<String, BigDecimal> balancesView() {
        return Map.copyOf(balances);
    }
}
