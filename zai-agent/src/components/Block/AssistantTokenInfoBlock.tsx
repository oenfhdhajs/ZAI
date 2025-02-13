import tokenInfoSvg from "@/assets/ic_token_info.svg";
import React from "react";
import {t} from "i18next";

interface TokenInfo {
    name: string;
    img: string;
    des: string;
    text: string;
}

interface Props {
    token: TokenInfo;
}

const AssistantTokenInfoBlock: React.FC<Props> = ({token}) => {

  return (
      <div className="bg-white border rounded-2xl p-3 w-full shadow-border-message">
        <div className="flex flex-row">
          <img className="w-5 h-5" src={tokenInfoSvg as string}/>
          <span className="ml-2 text-primary2 text-4 font-medium">Token Info</span>
        </div>
        <div className="flex row mt-2.5">
          <img className="mr-3 rounded-24px w-8 h-8" src={token.img}/>
          <div className="flex flex-col ml-2 text-3.5 font-medium">
            <span className="text-primary2 leading-none">{token.name}</span>
            <span className="text-color_text_middle leading-none mt-1">{token.des}</span>
            <div className="text-color_text_middle mt-2 leading-normal">
                {token.text}
            </div>
          </div>
        </div>
      </div>
  );
};

export default AssistantTokenInfoBlock;
