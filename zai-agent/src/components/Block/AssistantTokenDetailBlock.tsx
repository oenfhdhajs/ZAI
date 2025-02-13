import tokenInfoSvg from "@/assets/ic_token_info.svg";
import React from "react";

interface TokenInfo {
    name: string;
    img: string;
    des: string;
    text: string;
    list: any[];
}

interface Props {
    token: TokenInfo;
}

const AssistantTokenDetailBlock: React.FC<Props> = ({token}) => {

  return (
      <div className="bg-white border rounded-2xl p-3 w-full shadow-border-message">
        <div className="flex flex-row">
          <img className="w-5 h-5" src={tokenInfoSvg as string}/>
          <span className="ml-2 text-primary2 text-4 font-medium">Token Info</span>
        </div>
        <div className="flex row mt-2.5">
          <img className="mr-3 rounded-24px w-8 h-8" src={token.img}/>
          <div className="flex flex-col ml-2 text-3.5">
            <span className="text-primary2 text-sm leading-none font-medium">{token.name}</span>
            <span className="text-color_text_middltexte text-sm leading-none mt-2 font-medium">{token.des}</span>
            <div className="text-primary2 mt-2 text-sm leading-normal whitespace-pre">
                {token.text}
            </div>
          </div>
        </div>
        <div className="ml-16 mt-4">
          <ChartComponent list={token.list}/>
        </div>
      </div>
  );
};

export default AssistantTokenDetailBlock;
