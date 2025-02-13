
import logo from "@/assets/agent/logo.webp";
import IcMore from "@/assets/ic_more.svg";
import IcDelete from "@/assets/ic_delete.svg";
import { clearChatList, requestChatList, useChatStore } from "@/store/chatStore";
import { Skeleton } from "primereact/skeleton";
import { OverlayPanel } from 'primereact/overlaypanel';
import { useUserStore } from "@/store/userStore";
import { delChat } from "@/api/ai/chiat";
import {Accordion, AccordionTab} from "primereact/accordion";
import arrowRightIcon from "@/assets/ic_arrow_right.svg";
import chatHistoryIcon from "@/assets/ic_chat_history.svg";

interface Props {
  isOpen: boolean;
  setOpen: (isOpen: boolean) => void;
}
const Sidebar: React.FC<Props> = ({ isOpen, setOpen }) => {

  const {t} = useTranslation();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const [loading, setLoading] = useState(true);
  const sidebarRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const chatList = useChatStore((state) => state.chatList);
  const userInfo = useUserStore((state) => state.userInfo);
  const op = useRef<OverlayPanel>(null);
  const [currentDelTarget, setCurrentDelTarget] = useState(null);
  const [activeIndex, setActiveIndex] = useState(0);
  const { showDialog, hideDialog } = useDialog();

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      setLoading(false);
    }, 1200)

    requestChatList();
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      clearTimeout(timeoutId);
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  useEffect(() => {
    console.log("Sidebar userInfo useEffect", userInfo);
    if (!userInfo) {
      clearChatList();
      navigate("/");
    } else {
      requestChatList();
    }
  }, [userInfo]);

  const handleClickOutside = (event: any) => {
    const isMdScreen = window.matchMedia("(min-width: 768px)").matches;
    if (isOpen && !isMdScreen) {
      if (sidebarRef.current && !sidebarRef.current.contains(event.target)) {
        setOpen(!isOpen);
      }
    }
  };

  const toggleSidebar = (e: any) => {
    setOpen(!isOpen);
  };

  const toChatPage = (messageId: string) => {
    navigate("/?chatId=" + messageId);
  };

  const handleMore = (e: any, item: any) => {
    e.stopPropagation();
    setCurrentDelTarget(item);
    op.current?.toggle(e);
  };
  
  const delChatGroup = () => {
    const chatIdParam = queryParams.get("chatId");
    console.log('delChatGroup', currentDelTarget, chatIdParam);
    if (currentDelTarget) {
      const messageId = (currentDelTarget as any).messageId;
      delChat(messageId).then((res) => {
        op.current?.hide();
        requestChatList();
        if (chatIdParam === messageId) {
          navigate("/");
        }
      });
    }
    hideDialog();
  }

  const openDelChatGroupDialog = () => {
    showDialog({
      content: (
          <DelectDialog onConfirm={delChatGroup} onCancel={hideDialog} />
      ),
  });
  }

  const customHeader = (options: any, index: number) => {
    return (
        <div className="flex justify-between items-center w-full cursor-pointer">
          <span className="text-12px font-medium text-[#56637E]">{options.header}</span>
          <img src={arrowRightIcon}
              className={`w-2.5 h-2.5 transition-transform duration-300 ${
                  activeIndex === index ? "rotate-90" : "rotate-0"
              }`}
          />
        </div>
    );
  };

  const handleTabChange = (e: any) => {
    setActiveIndex(e.index);
  };

  return (
    <div
      className={`bg-[#F9F9F9] text-grap-200 z-10 transition-all duration-200 ease-in-out fixed left-0 top-0 h-full shadow-2xl md:shadow-none ${isOpen ? 'w-64' : '-translate-x-full'
        }`}
        ref={sidebarRef}
    >
      <div className="flex justify-between items-center px-4 h-54px w-64">
        <img src={logo} className="h-[34px]" />

        <button onClick={toggleSidebar} className={`text-black focus:outline-none ${isOpen ? '' : 'hidden'}`}>
          <ToggleSidebarSvg />
        </button>

      </div>
      {/*<div className="flex justify-start items-center mt-8 pl-4 h-25px w-64 text-[#56637E] text-12px">
        <span>Today</span>
      </div>*/}
      <div className="flex items-center mt-4 ml-4">
        <img className="w-4 h-4" src={chatHistoryIcon} />
        <span className="ml-3 text-black">{ t("home.chat_history") }</span>
      </div>
      <div className="flex flex-col justify-start items-start mt-2 px-2 text-14px overflow-hidden">
        {
          loading ? (<>
            <Skeleton width="6rem" height="1.25rem" className="ml-2 mt-2"></Skeleton>
            <Skeleton width="12rem" height="1.25rem" className="ml-2 mt-2"></Skeleton></>
          ) : (<>
            <Accordion className="w-full" activeIndex={activeIndex} onTabChange={handleTabChange} >
              {
                chatList.map((item: any, index: any) => (
                    <AccordionTab key={item.header} header={item.header} headerTemplate={(options) => customHeader(options, index)}
                    pt={{
                      headerIcon: {
                        style: {
                          display: "none"
                        }
                      },
                      headerAction: {
                        style: {
                          padding: "0.5rem",
                          backgroundColor: "transparent"
                        }
                      },
                      content: {
                        style: {
                          padding: "0",
                          fontSize: "14px",
                          color: "black",
                          backgroundColor: "transparent"
                        }
                      },
                    }}>
                      {item.children.map((block: any) => (
                          <div key={`chat-block-${block.messageId}`}
                               className="group w-full flex flex-row h-34px px-2 justify-start items-center hover:bg-[#EDEDED] hover:rounded-8px"
                               onClick={() => toChatPage(block.messageId)}>
                            <div className="flex-1 w-full flex justify-start items-center">
                            <span className="w-full whitespace-nowrap overflow-hidden text-ellipsis">
                              {block.title}
                            </span>
                              <img src={IcMore}
                                   className="opacity-0 group-hover:opacity-100 transition-opacity duration-300 hover:bg-[#EDEDED]"
                                   onClick={(e) => handleMore(e, block)}/>
                            </div>
                          </div>
                      ))}
                    </AccordionTab>
                ))
              }
            </Accordion>
          </>)
        }
      </div>
      <OverlayPanel ref={op}>
        <div className="flex flex-row bg-background px-4 py-2 hover:bg-[#EDEDED]">
          <img src={IcDelete}/>
          <span className="p-0 ml-2" onClick={openDelChatGroupDialog}>Delete</span>
        </div>
      </OverlayPanel>
    </div>
  );
};

export default Sidebar;