package cn.z.zai.common.constant;

import cn.z.zai.common.enums.ResponseCodeEnum;

/**
 * @Description:**
 */
public interface ResponseCodeConstant {

    //==========global success=============
    ResponseCodeEnum SUCCESS_GLOBAL_DEFAULT = new ResponseCodeEnum(200,"success");


    //==========global error=============
    ResponseCodeEnum ERROR_GLOBAL_PARAM = new ResponseCodeEnum(400,"param invalid");

    ResponseCodeEnum ERROR_GLOBAL_UN_AUTH = new ResponseCodeEnum(401,"user unAuth");

    ResponseCodeEnum ERROR_GLOBAL_USER_NOT_FOUNT = new ResponseCodeEnum(402,"user not found");

    ResponseCodeEnum UPGRADE = new ResponseCodeEnum(408, "upgrading");

    ResponseCodeEnum ERROR_GLOBAL_METHOD_NOT_ALLOWED = new ResponseCodeEnum(405,"METHOD_NOT_ALLOWED");

    ResponseCodeEnum ERROR_GLOBAL_DEFAULT = new ResponseCodeEnum(500,"Server exception, please try again");



    ResponseCodeEnum ERROR_BUSINESS_INIT_DATA_EXPIRE = new ResponseCodeEnum(1002,"InitData expire");


    ResponseCodeEnum ERROR_BOT_BUILD_SEND_TEXT = new ResponseCodeEnum(1501,"build bot send text exception");


}
