import React from "react";
import { t } from "i18next";
import tokenInfoSvg from "@/assets/ic_token_info.svg";
import { useConnection, useWallet } from "@solana/wallet-adapter-react";
import { walletSignTransaction } from "@/utils/walletUtil";
import { checkTransStatus, pushTransStatus, sendTrans } from "@/api/transaction";
import {handleTransactionFailedToast, sleep} from "@/utils/utils";
import {showToast, TOAST_TIME} from "@/store/toastStore.ts";

interface SwapTokenInfo {
    oneQuestId: string;
    name: string;
    img: string;
    needSol: string;
    text: string;
    transferStatus: number;
    code: number;
}

interface Props {
    token: SwapTokenInfo;
}
const AssistantBuyBlock: React.FC<Props> = ({ token }) => {
    const { signTransaction } = useWallet();
    const { connection } = useConnection();
    const [loading, setLoading] = useState(false);
    const [transferStatus, setTransferStatus] = useState(token.transferStatus || 0); // 0: creating 1: failed 2: successfull

    const toBuy = async () => {
        console.log("toBuy", token);
        if (loading || transferStatus === 2) {
            return;
        }
        if (token.code !== 200) {
            const hint = handleTransactionFailedToast(token.code);
            showToast("error", hint, TOAST_TIME);
            return;
        }
        try {
            setLoading(true);
            const serializedTransaction = await walletSignTransaction(token.text, signTransaction, connection);
            console.log(serializedTransaction);
            const res = await sendTrans({
                transaction: serializedTransaction,
            });
            console.log(res);
            const status = await checkStatus(res.transaction);
            if (status === "success") {
                toBuySuccess();
            } else {
                toBuyFailure();
            }
        } catch (e) {
            console.log(e);
        } finally {
            setLoading(false);
        }
    };

    const toBuySuccess = () => {
        console.log("toBuySuccess", token.oneQuestId);
        setTransferStatus(2);
        pushTransStatus({
            "action": "buyToken",
            "oneQuestId": token.oneQuestId,
            "transferStatus": 2
        });
    };

    const toBuyFailure = () => {
        console.log("toBuyFailure", token.oneQuestId);
        setTransferStatus(1);
        pushTransStatus({
            "action": "buyToken",
            "oneQuestId": token.oneQuestId,
            "transferStatus": 1
        });
    };

    const checkStatus = async (transaction: string) => {
        let num = 0;
        await sleep(5000);
        while (num < 15) {
            try {
                const res = await checkTransStatus({
                    transaction,
                });
                console.log(res);
                if (res.confirmationStatus === "finalized") {
                    return "success";
                }
            } catch (e) {
                console.log(e);
            } finally {
                num++;
                await sleep(5000);
            }
        }

        return "failure";
    };

    return (
        <div className="flex flex-col">
            <div className="bg-white border rounded-2xl pb-3 px-3 pt-4 w-full shadow-border-message">
                <div className="flex flex-row">
                    <img className="w-5 h-5" src={tokenInfoSvg} />
                    <span className="ml-2 text-primary2 text-4 font-medium">Buy Token</span>
                </div>
                <div className="text-color_text_middle text-sm leading-none mt-2">
                    <span>Amount:<span className="mr-1">{token.needSol}</span>SOL</span>
                </div>
                <div onClick={toBuy}
                    className={`rounded-8px w-full h-38px text-white mt-4 mb-3 flex items-center justify-center text-3.5 ${transferStatus === 2 ? "bg-color_text_middle" : "bg-primary1 hover:bg-[#EA2EFE80]"}`}>
                    {loading ? <><span>{t("chat.buy")}...</span><i className="pi pi-spin pi-spinner text-white ml-2"></i></> : <><span>{transferStatus === 2 ? t("chat.transfer_success")
                        : transferStatus === 1 ? t("chat.retry") : t("chat.buy")}</span></>}
                </div>
                <span className="text-red_text">{transferStatus === 1 ? t("chat.transfer_failed") : ""}</span>
            </div>
            <span className="font-medium text-3.5 leading-normal text-primary2 mt-2">{transferStatus === 1 ? t("chat.transfer_fail_message") : ""}</span>
        </div>
    );
};

export default AssistantBuyBlock;
