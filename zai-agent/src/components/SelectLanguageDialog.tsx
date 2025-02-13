// import dialogCloseImg from "@/assets/ic_dialog_close.svg";
import closeBlack from "@/assets/ic_close_black.svg";
import {t} from "i18next";
import {getDefaultLanguage, langs, setLocale} from "@/i18n";

const SelectLanguageDialog = (props: any) => {
    const [selectedLanguage, setSelectedLanguage] = useState(getDefaultLanguage())

    const closeDialog = () => {
        props.onCancel();
    };

    const changeLocale = (locale) => {
        setLocale(locale);
        setSelectedLanguage(locale)
        closeDialog();
        // this.locale = getLocale();
        // window.sessionStorage.setItem(offline_flag, "1");
        // window.location.reload();
    }
    return (
        <div>
            <div className="flex flex-col items-center mt-6 relative px-6 pb-10 bg-message_bg rounded-24px pt-6">
                <span className="text-primary2 text-1rem mb-3 w-full">{ t("home.select_language_dialog_title") }</span>
                <div className="bg-black_6 h-1px w-full"></div>
                <div className="grid grid-rows-4 grid-cols-4 mt-5">
                    {
                        Object.entries(langs).map(([key, item]) => {
                            return <div className={`text-14px font-medium pl-4 mr-4 w-44 mb-3 rounded-8px h-8 flex items-center justify-start cursor-pointer ${key === selectedLanguage ? "bg-black_6 text-primary1" : ""}`} key={key} onClick={() => changeLocale(key)}>
                                {item}
                            </div>
                        })
                    }
                </div>
                <img className="absolute top-5 right-6 w-6 h-3 cursor-pointer" src={closeBlack as string} onClick={() => closeDialog()} />
            </div>
        </div>
    );
};

export default SelectLanguageDialog;