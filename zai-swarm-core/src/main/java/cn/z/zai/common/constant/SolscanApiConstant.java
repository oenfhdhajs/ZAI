package cn.z.zai.common.constant;


public interface SolscanApiConstant {

    String HEADER_TOKEN_NAME = "token";

    @Deprecated
    String HEADER_TOKEN_VALUE = "xxxx";




    String BASE = "https://pro-api.solscan.io/v2.0";

    /**
     * @link {https://pro-api.solscan.io/pro-api-docs/v2.0/reference/v2-token-list}
     */
    String COIN_LIST_URL_GET = BASE + "/token/list";

    /**
     * @link {https://pro-api.solscan.io/pro-api-docs/v2.0/reference/v2-token-meta}
     */
    String COIN_META_URL_GET = BASE + "/token/meta";

    /**
     * @link {https://pro-api.solscan.io/pro-api-docs/v2.0/reference/v2-token-trending}
     */
    String TOKEN_TRENDING_URL_GET = BASE + "/token/trending";

    /**
     * https://pro-api.solscan.io/pro-api-docs/v2.0/reference/v2-token-transfer
     */
    String TOKEN_TRANSFER_URL_GET = BASE + "/token/transfer";



    /**
     * https://pro-api.solscan.io/pro-api-docs/v2.0/reference/v2-account-detail
     */
    String ACCOUNT_DETAIL_URL_GET = BASE + "/account/detail";

    /**
     * https://pro-api.solscan.io/v2.0/transaction/detail
     */
    String  TRANSACTION_DETAIL_URL_GET =BASE + "/transaction/detail";
}
