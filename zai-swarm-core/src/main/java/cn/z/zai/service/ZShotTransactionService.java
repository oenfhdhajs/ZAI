package cn.z.zai.service;

import cn.z.zai.dto.response.ZShotTransactionResponse;
import cn.z.zai.dto.response.ZShotTransactionSignatureResponse;
import cn.z.zai.dto.vo.ZShotTransactionSwapVo;
import cn.z.zai.dto.vo.ZShotTransactionTransferVo;

public interface ZShotTransactionService {


    /**
     *
     *
     * @param vo
     * @return
     */
    ZShotTransactionResponse<ZShotTransactionSignatureResponse> transfer(ZShotTransactionTransferVo vo);

    /**
     *
     *
     * @param vo
     * @return
     */
    ZShotTransactionResponse<ZShotTransactionSignatureResponse> swap(ZShotTransactionSwapVo vo);


}
