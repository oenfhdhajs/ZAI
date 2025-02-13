
import Chat from "@/components/Chat";
import MessageBox, { MessageBoxHandles } from "@/components/MessageBox";
import { ChatMessage, createTextChunk, MessageType, Role } from "@/models/ChatCompletion";
import Sidebar from "@/components/Sidebar";
import style1 from "./layout.module.css";
import { aiChat, getChatLog } from "@/api/ai/chiat";
import {checkSignMessage, debounceCallback} from "@/utils/utils";
import SendGuide from "@/components/SendGuide.tsx";
import {Image} from "primereact/image";
import messageGirl from "@/assets/ic_message_girl.png";
import FunctionsIcon from "@/assets/ic_functions.svg";
import transferIcon from "@/assets/ic_transfer.svg";
import searchCrytoIcon from "@/assets/ic_search_crypto.svg";
import tradeCryptoIcon from "@/assets/ic_trade_crypto.svg";
import {t} from "i18next";
import { v4 as uuidv4 } from 'uuid';
import { clearTempQuestion, getTempQuestion, requestChatList, setTempQuestion } from "@/store/chatStore";
import {isLogin} from "@/store/userStore";
import {useDialog} from "@/components/GlobalDialogContext.tsx";
import {useWallet} from "@solana/wallet-adapter-react";

interface CompletionChunk {
  action: string;
  text: string;
  oneQuestId: number;
  id: string;
  object: string;
  created: number;
  model: string;
  choices: CompletionChunkChoice[];
}

interface CompletionChunkChoice {
  index: number;
  delta: {
    content: string;
  };
  finish_reason: null | string; // If there can be other values than 'null', use appropriate type instead of string.
}

const suggestionQuestion = [
  {
    id: 0,
    title: 'Functions',
    icon: FunctionsIcon,
    q: 'What can you do?'
  },
  {
    id: 1,
    title: 'Transfer',
    icon: transferIcon,
    q: 'Transfer SOL to a wallet'
  },
  {
    id: 2,
    title: 'Search crypto',
    icon: searchCrytoIcon,
    q: 'Search toke / contract'
  },
  {
    id: 3,
    title: 'Trade crypto',
    icon: tradeCryptoIcon,
    q: 'Trade on the solana chain'
  },
];

export default function ZAIAgent() {
  const location = useLocation();
  const navigate = useNavigate();
  const queryParams = new URLSearchParams(location.search);
  const [loading, setLoading] = useState(false);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const messageBoxRef = useRef<MessageBoxHandles>(null);
  const [allowAutoScroll, setAllowAutoScroll] = useState(true);
  const [isOpen, setIsOpen] = useState(true);
  const [chatId, setChatId] = useState("");
  const [chatListLoading, setChatListLoading] = useState(true);
  const { showSelectWalletDialog } = useDialog();
  const { wallet, connected, signMessage, publicKey, disconnect } = useWallet();

  useEffect(() => {
    console.log('ZAIAgent useEffect');
    const chatIdParam = queryParams.get("chatId");
    setChatListLoading(true);
    // checkTransStatus({
    //   transaction: "5BFfUDioyMjD2BCtPg6kb9UtZijo9KWg7paup7v1cELTUqXuLELti5ACnBmjARLCPdDJ1AM3hRXr7fdorH7h1Hqf",
    // }).then((res) => {
    //   console.log('checkTransStatus', res.account, res.confirmationStatus);
    // });
    if (chatIdParam) {
      console.log("chatIdParam", chatIdParam);
      setChatId(chatIdParam);
    } else {
      setChatId("");
      setMessages([]);
    }
  }, [location.search]);

  useEffect(() => {
    if (chatId !== "") {
      const lastQuestion = getTempQuestion();
      if (lastQuestion !== "") {
        setChatListLoading(false);
        setMessages([]);
        clearTempQuestion();
        callApp(lastQuestion);
      } else {
        getChatLog(chatId).then((res) => {
          setChatListLoading(false);
          setMessages(res);
          if (res.length === 0) {
            navigate("/");
          }
        });
      }
    }
  }, [chatId])

  useEffect(() => {
    if (window.innerWidth < 768) {
      setOpen(false);
    }
  }, [])

  const callApp = async (message: string) => {
    if (message === "") {
      return;
    }
    if (!wallet || !connected) {
      showSelectWalletDialog();
      return;
    }
    if (!isLogin()) {
      // showToast("error", "Please log in first.", 3000)
      checkSignMessage(wallet, connected, publicKey, signMessage, disconnect);
      return;
    }

    console.log("callApp", chatId);
    if (chatId === "") {
      setTempQuestion(message);
      console.trace("callApp");
      navigate("/?chatId=" + uuidv4());
      return;
    }
    setLoading(true);
    const chunk = {
      action: "text",
      text: message,
    }
    addMessage(Role.User, MessageType.Normal, message, JSON.stringify(chunk), sendMessage);
  };

  const addMessage = (role: Role, messageType: MessageType, content: string, chunk: string, callback?: (callback: ChatMessage[]) => void) => {
    setMessages((prevMessages: ChatMessage[]) => {
      const message: ChatMessage = {
        id: prevMessages.length + 1,
        role: role,
        messageType: messageType,
        content: content,
        showContent: chunk,
      };
      // console.log(message);
      return [...prevMessages, message];
    });

    const newMessage: ChatMessage = {
      id: messages.length + 1,
      role: role,
      messageType: messageType,
      content: content,
      showContent: chunk,
    };
    const updatedMessages = [...messages, newMessage];
    
    if (callback) {
      callback(updatedMessages);
    }
  };

  const handleUserScroll = (isAtBottom: boolean) => {
    setAllowAutoScroll(isAtBottom);
  };

  const clearInputArea = () => {
    messageBoxRef.current?.clearInputValue();
  };

  // content: 
  const handleStreamedResponse = (content: string[]) => {
    setMessages(prevMessages => {
      let isNew: boolean = false;
      try {
        // todo: this shouldn't be necessary
        if (prevMessages.length == 0) {
          console.error('prevMessages should not be empty in handleStreamedResponse.');
          return [];
        }
        if ((prevMessages[prevMessages.length - 1].role == Role.User)) {
          isNew = true;
        }
      } catch (e) {
        console.error('Error getting the role')
        console.error('prevMessages = ' + JSON.stringify(prevMessages));
        console.error(e);
      }
      const chunks = content.map(res => JSON.parse(res));
      console.log("isNew", isNew);
      if (isNew) {
        const message: ChatMessage = {
          id: prevMessages.length + 1,
          role: Role.Assistant,
          messageType: MessageType.Normal,
          content: "content",
          showContent: JSON.stringify(chunks),
        };
        // console.log(message);
        return [...prevMessages, message];
      } else {
        // Clone the last message and update its content
        const showContent = prevMessages[prevMessages.length - 1].showContent;
        let chunkList = JSON.parse(showContent);
        let lastChunk = chunkList[chunkList.length - 1];

        let newChunks: any[] = [];
        newChunks.push(lastChunk);
        chunks.forEach((chunk) => {
          if (lastChunk.text === "Thinking...") {
            lastChunk = chunk;
            newChunks = [lastChunk];
          } else if (chunk.action !== "text" || lastChunk.action !== "text") {
            newChunks.push(chunk);
            lastChunk = chunk;
          } else {
            lastChunk = createTextChunk(lastChunk.text + chunk.text);
            newChunks = [...newChunks.slice(0, -1), lastChunk];
          }
        });

        chunkList = [...chunkList.slice(0, -1), ...newChunks];
        const updatedMessage = {
          ...prevMessages[prevMessages.length - 1],
          content: "content",
          showContent: JSON.stringify(chunkList),
        };

        console.log("updatedMessage", updatedMessage);
        // Replace the old last message with the updated one
        return [...prevMessages.slice(0, -1), updatedMessage];
      }
    });
  };

  async function sendMessage(updatedMessages: ChatMessage[]) {
    setLoading(true);
    clearInputArea();

    const msg = updatedMessages[updatedMessages.length - 1].content;
    const debouncedCb  = debounceCallback(handleStreamedResponse);
    debouncedCb([JSON.stringify(createTextChunk("Thinking..."))]);
    const response = await aiChat(msg, chatId);
    const reader = response.body.getReader();
    const decoder = new TextDecoder("utf-8");
    let partialDecodedChunk = undefined;
    try {
      // eslint-disable-next-line no-constant-condition
      while (true) {
        const streamChunk = await reader.read();
        const {done, value} = streamChunk;
        if (done) {
          break;
        }
        let DONE = false;
        let decodedChunk = decoder.decode(value);
        if (partialDecodedChunk) {
          decodedChunk = "data:" + partialDecodedChunk + decodedChunk;
          partialDecodedChunk = undefined;
        }
        
        const rawData = decodedChunk.split("data:").filter(Boolean);  // Split on "data: " and remove any empty strings
        const chunks: CompletionChunk[] = rawData.map((chunk, index) => {
          partialDecodedChunk = undefined;
          chunk = chunk.trim();
          if (chunk.length == 0) {
            return;
          }
          let o;
          try {
            o = JSON.parse(chunk);
            if (o.action === 'stop') {
              DONE = true;
              return;
            }
          } catch (err) {
            if (index === rawData.length - 1) { // Check if this is the last element
              partialDecodedChunk = chunk;
            } else if (err instanceof Error) {
              console.error(err.message);
            }
          }
          return o;
        }).filter(Boolean); // Filter out undefined values which may be a result of the [DONE] term check

        console.log("chunks size", chunks.length);
        const accumulatedContetList: string[] = [];
        chunks.forEach(chunk => {
          accumulatedContetList.push(JSON.stringify(chunk));
        });
        if (accumulatedContetList.length !== 0) {
          debouncedCb(accumulatedContetList);
        }

        if (DONE) {
          return;
        }
      }
    } catch (error) {
      if (error instanceof Error && error.name === 'AbortError') {
        // User aborted the stream, so no need to propagate an error.
      } else if (error instanceof Error) { /* empty */ } else {
        console.error('An unexpected error occurred');
      }
      return;
    } finally {
      setLoading(false); // Stop loading here, whether successful or not
      requestChatList();
    }
  }
  const setOpen = (open: boolean) => {
    setIsOpen(open);
  }

  const clickQuickQuestion = (question: string) => {
    messageBoxRef.current?.askQuickQuestion(question);
  };

  return (
    <main className="h-screen flex">
      <Sidebar isOpen={isOpen} setOpen={setOpen}/>

      <div className={`h-screen w-full flex flex-col relative transition-all duration-200 ease-in-out overflow-hidden ${style1.root_chat}  ${isOpen ? 'md:ml-64' : 'ml-0'}`}>
        <MainHeader isOpen={isOpen} setOpen={setOpen}/>

        <div className="overflow-auto w-full h-full z-36 pb-48">

          {/* <img className="w-[560px]" src={icAgentCharacter} alt="" />
          <div className="text-primary2 text-32px">{t('agent.title')}</div> */}
          <div className="h-full flex flex-col justify-center items-center md:max-w-3xl lg:max-w-[40rem] xl:max-w-[48rem] mx-auto">
            {
              chatId === "" ? <SendGuide/> : (chatListLoading ? <i className="pi pi-spin pi-spinner text-gray-600" style={{ fontSize: '2rem' }}></i> : <Chat chatBlocks={messages} onChatScroll={handleUserScroll} allowAutoScroll={allowAutoScroll}
                loading={loading}/>)
            }

            <div
                className="absolute bottom-24 flex flex-row w-full md:max-w-3xl lg:max-w-[40rem] xl:max-w-[48rem] mb-1"
                style={{zIndex: 4}}>
              <div className="w-fit flex flex-row justify-end items-end"><Image className="w-16 ml-5"
                                                                                src={messageGirl as string}/>
              <div className="bg-primary1 text-white flex items-center justify-center" style={{
                  borderRadius: "9px 9px 9px 0",
                  width: "18px",
                  height: "18px",
                  fontSize: "10px",
                  paddingTop: "2px",
                  marginLeft: "-7px",
                  marginBottom: "60px",
                }}>Hi
                </div>
              </div>
              <div className="flex flex-col ml-2 h-full items-start">
                    <div className="text-color_text_light">{t("chat.message_suggestion")}</div>
                    <div className={`grid ${chatId === "" ? 'grid-cols-2' : 'grid-cols-[auto_auto_auto_auto]'}  gap-2.5 mt-2 flex-wrap`}>
                    {
                    suggestionQuestion.map((item) => {
                      return <div
                          key={`quesstion-${item.id}`}
                          className="flex flex-row justify-start items-center text-color_text_dark text-14px bg-white_50 w-auto px-10px py-2.5 whitespace-nowrap hover:bg-[#E4E4E780]"
                          style={{borderRadius: "12px", border: "1px solid white"}} onClick={() => clickQuickQuestion(item.q)}>
                            {
                              chatId !== "" ? <><Image width="18px" src={item.icon} /><span className="ml-3">{item.title}</span></> : <>{item.q}</>
                            }
                        </div>
                    })
                  }
                </div>
              </div>
            </div>

            <MessageBox
                ref={messageBoxRef}
                callApp={callApp}
                loading={loading}
                setLoading={setLoading}
                allowImageAttachment={""}/>
          </div>
        </div>
      </div>
    </main>
  );
}
