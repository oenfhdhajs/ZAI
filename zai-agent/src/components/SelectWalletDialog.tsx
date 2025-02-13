// import dialogCloseImg from "@/assets/ic_dialog_close.svg";
import dialogCloseImg from "@/assets/ic_dialog_close_light.svg";
import phantomImg from "@/assets/ic_wallet_phantom.png";
import okxImg from "@/assets/ic_wallet_okx.png";
import solflareImg from "@/assets/ic_wallet_solflare.png";
import {useConnectWallet} from "@/utils/walletUtil.ts";
import {WalletName} from "@solana/wallet-adapter-base";

const SelectWalletDialog = (props: any) => {
    const {t} = useTranslation();
    const connectWallet = useConnectWallet();
    const walletList = [
        { name: "Phantom (Solana)", id: 1, img: phantomImg, downloadUrl: "https://phantom.com/download" },
        { name: "OKX (Solana)", id: 2, img: okxImg, downloadUrl: "https://www.okx.com/download" },
        { name: "Solflare (Solana)", id: 3, img: solflareImg, downloadUrl: "https://solflare.com" },
    ];

    const setVisible = async (item: any) => {
        switch (item.id) {
            case 1:
                connectWallet("Phantom" as WalletName, item.downloadUrl);
                break;
            case 2:
                connectWallet("OKX Wallet" as WalletName, item.downloadUrl);
                break;
            case 3:
                connectWallet("Solflare" as WalletName, item.downloadUrl);
                break;
        }
        props.onConfirm(item);
    }

    const closeDialog = () => {
        props.onCancel();
    };

    return (
        <div>
            <div className="flex flex-col w-full md:w-500px items-center mt-6 relative px-6 pb-10 bg-background rounded-24px pt-6">
                <span className="text-primary2 text-xl mb-4 px-4 text-center">{ t("home.select_wallet_dialog_title") }</span>
                {
                    walletList.map((item, index) => {
                        return <div
                            className="flex items-center w-full px-5 py-3 cursor-pointer border border-solid mt-4 mx-6 rounded-18px hover:bg-[#0000000A]"
                            key={index}
                            onClick={() => setVisible(item)}>
                            <img src={item.img}/>
                            <span className="text-primary2 ml-4 font-600">{`${item.name}`}</span>
                        </div>
                    })
                }
                <img className="absolute right-6 w-6 h-6 cursor-pointer" src={dialogCloseImg} onClick={() => closeDialog()} />
            </div>
        </div>
    );
};

export default SelectWalletDialog;