import React from 'react';
import ToggleSidebarSvg from "@/components/ToggleSidebarSvg";
import NewChatSvg from "@/components/NewChatSvg";
import WalletStatus from "@/components/WalletStatus.tsx";
import whiteSettingIcon from "@/assets/ic_setting_white.svg";
import blackSettingIcon from "@/assets/ic_setting_black.svg";
import {OverlayPanel} from "primereact/overlaypanel";
import { Tooltip } from 'primereact/tooltip';
import {t} from "i18next";
import {getSelectedLanguage} from "@/i18n";
import {useDialog} from "@/components/GlobalDialogContext.tsx";
import SelectLanguageDialog from "@/components/SelectLanguageDialog.tsx";
import { getChatPre } from '@/api/ai/chiat';

interface Props {
  isOpen: boolean;
  setOpen: (isOpen: boolean) => void;
}
const MainHeader: React.FC<Props> = ({ isOpen, setOpen }) => {
  // const location = useLocation();
  const navigate = useNavigate();
  // const [currentIndex, setCurrentIndex] = useState(0);
  const { theme, setTheme } = useTheme();
  const overlayPanelRef = useRef<OverlayPanel>(null);
  const { showDialog, hideDialog } = useDialog();

  // useEffect(() => {
  //   const pathname = location.pathname;
  //   switch (pathname) {
  //       case "/":
  //           setCurrentIndex(0);
  //           break;
  //       case "/agent":
  //           setCurrentIndex(1);
  //           break;
  //   }
  // }, [location.pathname]);

  // const toZAIAgent = () => {
  //   navigate('/agent');
  // };

  // setTheme(theme === "light" ? "dark" : "light")

  const toggleSidebar = (e: any) => {
    setOpen(!isOpen);
  };

  const newChatSidebar = async (e: any) => {
    await getChatPre();
    navigate("/");
  }

  const showSettingPanel = (event: any) => {
      overlayPanelRef.current?.toggle(event);
  }

  const showSelectLanguageDialog = () => {
      showDialog({
          content: (
              <SelectLanguageDialog onConfirm={hideDialog} onCancel={hideDialog} />
          ),
      });
  }

  return (
    <div className="w-full flex justify-between items-center h-[60px] ">
        <div className="px-4">
          <button onClick={toggleSidebar} className={`transition-all duration-200 text-black focus:outline-none ${isOpen ? 'hidden' : 'opacity-1'}`}>
            <ToggleSidebarSvg />
          </button>
          <button onClick={newChatSidebar} className={`tips ml-2 text-black focus:outline-none`} data-pr-tooltip="New Chat" >
            <NewChatSvg />
          </button>
          <Tooltip target=".tips" position='bottom'/>
        </div>

        <img className="ml-auto hidden" src={(theme === "light" ? blackSettingIcon : whiteSettingIcon) as string} onClick={showSettingPanel} />
        <div className="flex items-center h-[34px] ml-11 mr-4">
            <WalletStatus />
        </div>
        <OverlayPanel ref={overlayPanelRef}>
            <div className={`${theme === 'light' ? "bg-message_bg" : "bg-white"} rounded-14px flex flex-col w-72`} style={{ padding: "16px 0" }}>
                <div className="flex flex-row items-center bg-black_4 text-primary2 text-14px h-9">
                    <div className="flex-1 ml-4">{t("chat.language")}</div>
                    <div className="mr-4 w-24 h-30px rounded-8px bg-black_6 flex items-center justify-center" onClick={showSelectLanguageDialog}>{ getSelectedLanguage() }</div>
                </div>
            </div>
        </OverlayPanel>
    </div>
  );
}

export default MainHeader;