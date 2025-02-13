import {useWallet} from "@solana/wallet-adapter-react";
import {VersionedTransaction} from "@solana/web3.js";
import type {WalletName} from "@solana/wallet-adapter-base";
import bs58 from 'bs58'
import {showToast, TOAST_TIME} from "@/store/toastStore.ts";
import {t} from "i18next";

export const signMessageWithWallet = async (signMessage: ((message: Uint8Array) => Promise<Uint8Array>) | undefined, nonce: string) => {
    if (!signMessage) {
        throw new Error('Wallet does not support message signing!');
    }
    try {
        const message = `By signing, you agree to ZAI's Terms of Use and Privacy Policy. Your authentication nonce is:${nonce}`
        // console.log("message:", message)
        const encodedMessage = new TextEncoder().encode(message);
        const signedMessage = await signMessage(encodedMessage);
        const signature = bs58.encode(signedMessage);
        // console.log("signature:", signature)
        return signature;
    } catch (error) {
        console.error('sign message error:', error);
        throw new Error('sign message error, please try again');
    }
}

export const useIsWalletConnected = () => {
    const { wallet, connected } = useWallet();
    console.log("useIsWalletConnected:", wallet?.readyState, connected)
    return wallet && connected;
};
export const useConnectWallet = () => {
    const { wallets, connecting, connected, select, connect } = useWallet();

    return useCallback(
        async (walletName: WalletName, downloadUrl: string) => {
            if (wallets && wallets.filter((it) => it.adapter.name === walletName).length === 0) {
                window.open(downloadUrl);
                return;
            }
            if (!connected && !connecting) {
                await select(walletName);
                await connect();
            }
        }, []
    );
};

const versionedTransactionToBase64 = (transaction: VersionedTransaction) => {
    const serializedTransaction = transaction.serialize();
    const binaryString = String.fromCharCode(...serializedTransaction);
    return btoa(binaryString);
  }

  
export const walletSignTransaction = async (transactionStr: string, signTransaction: any, connection: any) => {
    const transaction = VersionedTransaction.deserialize(Buffer.from(transactionStr, 'base64'));
    console.log(transaction);
    if (!signTransaction) {
        showToast("error", t("home.sign_not_available"), TOAST_TIME);
        return;
    }

    
    const signedTransaction = await signTransaction(transaction);
    return versionedTransactionToBase64(signedTransaction);
    // const serializedTransaction = signedTransaction.serialize();
    // const signature = await connection.sendTransaction(xxx, {maxRetries: 2, preflightCommitment: "confirmed"});
    // return {signature, xxx};
}
