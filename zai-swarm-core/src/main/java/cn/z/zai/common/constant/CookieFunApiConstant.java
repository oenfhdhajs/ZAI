package cn.z.zai.common.constant;

public interface CookieFunApiConstant {

    /**
     * api https://docs.cookie.fun/#/api/endpoints?id=get-agent-by-contract-address
     */

    String HEADER_API_KEY_VALUE = "xxx";

    String BASE = "https://api.cookie.fun/v2/agents";

    String CONTRACT_ADDRESS = BASE + "/contractAddress/%s";
}
