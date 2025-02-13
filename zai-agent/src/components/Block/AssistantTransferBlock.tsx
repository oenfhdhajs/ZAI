import React from "react";
import { t } from "i18next";
import IcBalanceCheckSvg from "@/assets/ic_balance_check.svg";
import { useConnection, useWallet } from "@solana/wallet-adapter-react";
import {handleTransactionFailedToast, sleep} from "@/utils/utils";
import { walletSignTransaction } from "@/utils/walletUtil";
import { checkTransStatus, pushTransStatus, sendTrans } from "@/api/transaction";
import sufficientSvg from "@/assets/ic_sufficient.svg";
import {showToast, TOAST_TIME} from "@/store/toastStore.ts";

interface TransferInfo {
    oneQuestId: string;
    name: string;
    img: string;
    text: string;
    needAmount: string;
    symbol: string;
    targetAccount: string;
    transferStatus: number;
    code: number;
}

interface Props {
    token: TransferInfo;
}
const AssistantTransferBlock: React.FC<Props> = ({ token }) => {

    const { signTransaction } = useWallet();
    const { connection } = useConnection();
    const [loading, setLoading] = useState(false);
    const [transferStatus, setTransferStatus] = useState(token.transferStatus || 0); // 0: creating 1: failed 2: successfull

    const toBuy = async () => {
        console.log("toBuy", token);
        if (loading) {
            return;
        }
        if (transferStatus === 2) {
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
                toTransferSuccess();
            } else {
                toTransferFailure();
            }
        } catch (e) {
            console.log(e);
        } finally {
            setLoading(false);
        }
    };

    const toTransferSuccess = () => {
        console.log("toTransferSuccess", token.oneQuestId);
        setTransferStatus(2);
        pushTransStatus({
            "action": "transferConfirmation",
            "oneQuestId": token.oneQuestId,
            "transferStatus": 2
        });
    };

    const toTransferFailure = () => {
        console.log("toTransferFailure", token.oneQuestId);
        setTransferStatus(1);
        pushTransStatus({
            "action": "transferConfirmation",
            "oneQuestId": token.oneQuestId,
            "transferStatus": 1
        });
    };

    const checkStatus = async (transaction: string) => {
        let num = 0;
        while (num < 15) {
            try {
                await sleep(5000);
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
            }
        }

        return "failure";
    };
    return (
        <div className="flex flex-col">
            <div className="bg-white border rounded-2xl pb-3 px-3 pt-4 w-full shadow-border-message">
                <div className="flex flex-row">
                    <img className="w-5 h-5" src={IcBalanceCheckSvg as string} />
                    <span className="ml-2 text-primary2 text-4 font-medium">Transfer Confirmation</span>
                </div>
                <div className="flex flex-row font-medium text-3.5 mt-2">
                    <div className="text-color_text_middle">{t("chat.amount")}</div>
                    <div className="flex-1 text-end">{token.needAmount} {token.symbol}</div>
                    {
                        transferStatus === 2 ? (
                            <>
                            <img className="w-5 h-5 ml-2 mr-1" src={sufficientSvg as string} />
                            <span className="text-[#58D714]">{t("chat.sufficient")}</span></>
                        ) : (<></>)
                        }
                </div>
                <div className="flex flex-row font-medium text-3.5 mt-2">
                    <div className="text-color_text_middle">{t("chat.to")}</div>
                    <div className="flex-1 text-end">{token.targetAccount}</div>
                </div>

                <div onClick={toBuy}
                    className={`rounded-8px w-full h-38px text-white mt-4 mb-3 flex items-center justify-center text-3.5 ${transferStatus === 2 || loading ? "bg-color_text_middle" : "bg-primary1 hover:bg-[#EA2EFE80]"}`}>
                    {loading ? <><span>transaction...</span><i className="pi pi-spin pi-spinner text-white ml-2"></i></> : <><span>{transferStatus === 2 ? t("chat.transfer_success")
                        : transferStatus === 1 ? t("chat.retry") : t("chat.sign_transaction")}</span></>}
                </div>
                <span className="text-red_text">{transferStatus === 1 ? t("chat.transfer_failed") : ""}</span>
            </div>
        </div>
    );
};

export default AssistantTransferBlock;
