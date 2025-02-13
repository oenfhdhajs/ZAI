package cn.z.zai.dto.response;

import lombok.Data;

@Data
public class ZShotTransactionSignatureResponse {

    public String id;

    public String transactionHash;

    public String transaction;
}
