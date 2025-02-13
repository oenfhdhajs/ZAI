import IcBalanceCheckSvg from "@/assets/ic_balance_check.svg";
import IcSolanaLogoSvg from "@/assets/ic_solana_logo.svg";
import insufficientSvg from "@/assets/ic_insufficient.svg";
import sufficientSvg from "@/assets/ic_sufficient.svg";
import { t } from "i18next";

interface BalanceCheckInfo {
  symbol: string;
  needCoin: number;
  userBalanceCoin: number;
}

interface Props {
  data: BalanceCheckInfo;
}

const AssistantBalanceCheckBlock: React.FC<Props> = ({ data }) => {

  return (
    <div className="bg-white border rounded-2xl p-3 w-full shadow-border-message">
      <div className="flex flex-row">
        <img className="w-5 h-5" src={IcBalanceCheckSvg as string} />
        <span className="ml-2 text-primary2 text-4 font-medium">{t("chat.balance_check")}</span>
      </div>
      <div className="flex flex-row mt-2.5">
        <img className="mr-3" src={IcSolanaLogoSvg as string} width={32} height={32} />
        <div className="flex flex-col ml-2 text-3.5 font-medium">
          <div className="flex flex-row items-center">
            <span className="text-primary2">{data.userBalanceCoin} {data.symbol}</span>
            {
              data.needCoin > data.userBalanceCoin ? (
                <>
                  <img className="w-5 h-5 ml-2 mr-1" src={insufficientSvg as string} />
                  <span className="text-red_text">{t("chat.insufficient")}</span></>
                  
              ) : (<>
                <img className="w-5 h-5 ml-2 mr-1" src={sufficientSvg as string} />
                <span className="text-[#58D714]">{t("chat.sufficient")}</span></>)
            }
          </div>
          <span className="text-color_text_middle">Solana</span>
        </div>
      </div>
    </div>
  );
};

export default AssistantBalanceCheckBlock;
