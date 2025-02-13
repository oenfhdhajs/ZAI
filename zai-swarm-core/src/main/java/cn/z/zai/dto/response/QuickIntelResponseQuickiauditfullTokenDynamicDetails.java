package cn.z.zai.dto.response;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class QuickIntelResponseQuickiauditfullTokenDynamicDetails {

    // Timestamp of the last time the honeypot / taxes were checked.
    private Long lastUpdatedTimestamp;

    // Indicates if the contract is a honeypot.
    private Boolean is_Honeypot;

    // Returns the current buy tax.
    private String buy_Tax;

    // Returns the current sell tax.
    private String sell_Tax;

    // Returns the current transfer tax.
    private String transfer_Tax;

    // Returns the sell tax in the case the contract has a trading cooldown.
    private String post_Cooldown_Tax;

    // The maximum tokens allowed in a single transaction.
    private Integer max_Transaction;

    // The maximum percentage of tokens allowed in a single transaction.
    private String max_Transaction_Percent;

    // he maximum tokens a single wallet is allowed to contain.
    private Integer max_Wallet;

    // The maximum percentage of tokens a single wallet is allowed to contain.
    private String max_Wallet_Percent;

    // The amount of the supply that has been burned.
    private Integer token_Supply_Burned;

    // The liquidity pairing address for the token. Note that depending on which endpoint you use, you will receive either the first LP found or a list of all LPs.
    private String lp_Pair;

    // The liquidity pairing supply for the token.
    private BigDecimal lp_Supply;

    // The percent of the LP that is burned.
    private String lp_Burned_Percent;

    // Returns the price impact of the transaction.
    private String  price_Impact;

    // Indicates if there was a problem with the test buy/sell.
    private Boolean problem;

    // Lists the description of the issue if problem = true.
    private String extra;

    private DynamicDetailsLpLocks lp_Locks;

    @Data
    private static class DynamicDetailsLpLocks {

        // Dex ID for one of the dexes on this chain. Note this will be different based on the chain.
        private Object pinksale;

        // Dex ID for one of the dexes on this chain. Note this will be different based on the chain.
        private Object dxsale;

        private DynamicDetailsLpLocksOnlymoons onlymoons;
    }

    @Data
    private static class DynamicDetailsLpLocksOnlymoons {

        // The timestamp of when the liquidity was locked.
        private Long lockDate;

        // The amount of liquidity that was locked.
        private Integer amount;

        // The timestamp of when the liquidity will become unlocked.
        private Long unlockDate;

        // The lock ID.
        private String lockID;

        // The owner's address.
        private String owner;

        // The percentage of liquidity locked.
        private String percentageLocked;
    }
}


