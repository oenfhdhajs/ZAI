import React, { useEffect } from "react";
import {useConnection, useWallet} from "@solana/wallet-adapter-react";
import walletImg from "@/assets/ic_home_wallet.svg";
import { useDialog } from '@/components/GlobalDialogContext.tsx';
import { Menu } from "primereact/menu";
import { MenuItem } from "primereact/menuitem";
import { t } from "i18next";
import {isLogin, logout, useUserStore} from "@/store/userStore.ts";
import {checkSignMessage} from "@/utils/utils.ts";
import {LAMPORTS_PER_SOL} from "@solana/web3.js";

interface Props {

}

const WalletStatus: React.FC<Props> = () => {
    const { wallet, connecting, connected, disconnecting, signMessage, disconnect, publicKey } = useWallet();
    const { showSelectWalletDialog } = useDialog();
    const menuRef = useRef<Menu>(null);
    const userInfo = useUserStore((state)=>state.userInfo)
    // const { connection } = useConnection();

    const rightMenuClick = (event: any) => {
        if (!wallet || !connected) {
            showSelectWalletDialog();
        } else {
            menuRef.current?.toggle(event);
        }
    }

    const splitWalletAddress = () => {
        const address = publicKey?.toBase58();
        if (address) {
            return address.substring(0, 8) + "...";
        }
        return "";
    }

    const signOut = () => {
        disconnect();
        window.localStorage.clear();
        logout();
    }

    const items: MenuItem[] = [
        {
            label: t("home.sign_out"),
            icon: 'pi pi-sign-out',
            command: () => {
                signOut();
            }
        }
    ];

    // const getBalance = async () => {
    //     console.log("eeeeeeee:", publicKey?.toBase58())
    //     connection.onAccountChange(
    //         publicKey,
    //         (updatedAccountInfo) => {
    //             console.log("balance111:", updatedAccountInfo.lamports / LAMPORTS_PER_SOL);
    //         },
    //         "confirmed",
    //     );
    //     connection.getAccountInfo(publicKey).then((info) => {
    //         console.log("balance222:", info, parseFloat(info?.lamports));
    //     });
    //
    //     const walletBalance = await connection.getBalance(publicKey);
    //     console.log("walletBalance:", walletBalance, parseFloat(walletBalance) / LAMPORTS_PER_SOL);
    // }

    useEffect(() => {
        console.log("wwwwwwwww:", connecting, disconnecting, connected, !isLogin());
        if (connected && !isLogin()) {
            checkSignMessage(wallet, connected, publicKey, signMessage, disconnect);
        }
        // if (connected) {
        //     getBalance();
        // }
    }, [connected]);

    useEffect(() => {
        if (userInfo && publicKey && userInfo.address !== publicKey.toBase58()) {
            signOut();
        }
    }, [publicKey, wallet, userInfo]);

    return (
        <div className="flex items-center gap-2 h-full px-2 border rounded-md cursor-pointer hover:bg-gray-200"
            onClick={(event) => rightMenuClick(event)}>
            <img className="w-4 h-4" src={walletImg} />
            <span className="text-primary2 text-14px">{userInfo && publicKey ? splitWalletAddress() : "Connect Wallet"}</span>
            <Menu className="text-14px" ref={menuRef} model={items} popup />
        </div>
    );
};

export default WalletStatus;