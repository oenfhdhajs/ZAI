package cn.z.zai.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class QuickIntelResponseQuickiauditfullQuickiAudit {

    // The address of the wallet which created the contract.
    private String contract_Creator;

    // The wallet address of the contract owner.
    private String contract_Owner;

    // The name of the contract.
    private String contract_Name;

    // The ID of the chain where the contract resides. For a list of supported chains and their IDs, please visit https://docs.quickintel.io/developers/api-supported-chains
    private String contract_Chain;

    // The address of the contract being audited.
    private String contract_Address;

    // Indicates if the contract is renounced.
    private Boolean contract_Renounced;

    // Indicates if the contract was launched from a launchpad.
    private Boolean is_Launchpad_Contract;

    // The information of the launchpad.
    private String launchpad_Details;

    // Indicates if the contract has a hidden owner.
    private Boolean hidden_Owner;

    // An array of modifiers that can run by a hidden owner.
    private List<String> hidden_Owner_Modifiers;

    // Indicates if the contract uses a proxy contract.
    private Boolean is_Proxy;

    // The address of the proxy contract if one exists.
    private String proxy_Implementation;

    // Indicates if the contract has embedded external contracts that can control this contract.
    private Boolean has_External_Contract_Risk;

    // List of external contracts this contract has enabled.
    private List<String> external_Contracts;

    // Indicates if the contract has one or more obfuscated addresses embedded in the contract. These are hidden address that can be either a contract or address masked as a numberic value.
    private Boolean has_Obfuscated_Address_Risk;

    // List of obfuscated addresses in the contract.
    private List<String> obfuscated_Address_List;

    // Indicates if the contract has one or more potential pregenerated addresses embedded in the contract that later turn into contracts. These are hidden address that can be either a contract or address masked as a numberic value.
    private Boolean has_Pregenerated_Contract_Address_Risk;

    // List of potential pregenerated addresses in the contract.
    private List<String> pregenerated_Contract_Address_List;

    // Indicates if the contract has the capability of minting tokens.
    private Boolean can_Mint;

    private Boolean can_Freeze_Trading;

    private Boolean is_Mutable;

    // Indicates if the contract is unable to mint tokens after the contract is renounced
    private Boolean cant_Mint_Renounced;

    // Indicates if the contract has the capability of burning tokens.
    private Boolean can_Burn;

    // Indicates if the contract has the capability of blacklisting wallets.
    private Boolean can_Blacklist;

    //  Indicates if the contract is unable to blacklist wallets after being renounced.
    private Boolean cant_Blacklist_Renounced;

    // Indicates if the contract has the capability of multi-blacklisting wallets.
    private Boolean can_MultiBlacklist;

    // Indicates if the contract has the capability of whitelisting wallets.
    private Boolean can_Whitelist;

    // Indicates if the contract is unable to whitelist wallets after being renounced.
    private Boolean cant_Whitelist_Renounced;

    // Indicates if the contract has the capability of updating taxes/fees.
    private Boolean can_Update_Fees;

    // Indicates if the contract is unable to update taxes/fees after being renounced.
    private Boolean cant_Update_Fees_Renounced;

    // Indicates if the contract has the capability of updating the max wallet.
    private Boolean can_Update_Max_Wallet;

    // Indicates if the contract is unable to update the max wallet after being renounced.
    private Boolean cant_Update_Max_Wallet_Renounced;

    // Indicates if the contract has the capability of updating the max transaction.
    private Boolean can_Update_Max_Tx;

    // Indicates if the contract is unable to update the max transaction after being renounced.
    private Boolean cant_Update_Max_Tx_Renounced;

    // Indicates if the contract has the capability to pause trading.
    private Boolean can_Pause_Trading;

    // Indicates if the contract is unable to pause trading after being renounced.
    private Boolean cant_Pause_Trading_Renounced;

    // Indicates if the contract has a trading cooldown.
    private Boolean has_Trading_Cooldown;

    // Indicates if the owner can change the wallet receiving taxes/fees.
    private Boolean can_Update_Wallets;

    // Indicates if the contract has suspicious functions. If true, see suspicious_Functions for a list of the functions and their code.
    private Boolean has_Suspicious_Functions;

    // Indicates if the contract has external functions. If true, see external_Functions for a list of the functions and their code.
    private Boolean has_External_Functions;

    // Indicates if the contract has a fee warning.
    private Boolean has_Fee_Warning;

    // Indicates if the contract has a modified transfer method.
    private Boolean has_ModifiedTransfer_Warning;

    // Array of modified transfer methods found in the contract.
    private List<String> modified_Transfer_Functions;

    // Suspicious Functions refers to actions within the contract that may not normally be found in a contract. This does not constitute any action to be good or bad, but is advised to review. The data is returned in an array of strings which contains the functions code and defaults to null if no functions were found.
    private List<String> suspicious_Functions;

    // External Functions are actions that can be ran EVEN when the contract has been renounced. The data is returned in an array of strings which contains the functions code and defaults to null if no functions were found.
    private List<String> external_Functions;

    // Audit Functions are functions related to the modification of taxes. The data is returned in an array of strings which contains the functions code and defaults to null if no functions were found.
    private List<String> audit_Functions;

    // Indicates if the contract has a matching signature with a known scam contract.
    private Boolean has_Scams;

    // List of matching scams. The data is returned in an array of strings which contains the scam contract's information and defaults to null if no functions were found.
    private String matched_Scams;

    // Scam Functions are known scams that is tracked by Quick Intel. This will return the matched scam, along with the scam data, when there is a match. The data is returned in an array and defaults to null if no matching scams were found.
    private List<String> scam_Functions;

    // Indicates if the creator of the token had funding from a known scam wallet.
    private Boolean has_Known_Scam_Wallet_Funding;


    // List of matching known scam wallets. The data is returned in an array of which contains the scam wallets's information and defaults to null if none were found.
    private String known_Scam_Wallet_Funding;

    // List of links (typically social media) that are found within the contract.
    private List<String> contract_Links;

    // Functions is the list of WRITE functions that are available. The data is returned in an array of strings with the function names and defaults to null if no functions were found.
    private List<String> functions;


    private List<String> onlyOwner_Functions;


    private List<String> singleBlacklistFunctions;

    // Multi-Blacklist Functions are potential actions that can be used to multi-blacklist wallets. The data is returned in an array of strings with the function names and defaults to null if no functions were found.
    private List<String> multiBlacklistFunctions;

    // Indicates if the contract has general vulnerabilities. This is a sort of catch-all for edge cases where we see this rarely.
    private Boolean has_General_Vulnerabilities;


    // Array of general vulnerabilities found in the contract.
    private List<String> general_Vulnerabilities;


    //  Indicates if the contract has a suspicious code that could allow the creator to steal tokens.
    private Boolean can_Potentially_Steal_Funds;


    // Array of suspicious code that could lead to stolen tokens found in the contract.
    private List<String> can_Potentially_Steal_Funds_Functions;

    private QuickIntelResponseQuickiauditAuthorities authorities;

}