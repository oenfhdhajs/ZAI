package cn.z.zai.common.constant;


public interface QuickNodeApiConstant {

    @Deprecated
    String API_KEY_VALUE = "xxxx";



    String BASE = "https://fabled-broken-darkness.solana-mainnet.quiknode.pro";

    /**
     * @link {https://www.quicknode.com/docs/solana/getTransaction}
     */
    @Deprecated
    String URL_POST = BASE + "/" + API_KEY_VALUE;

    String URL_POST_NEW = BASE + "/" + "%s";
}
