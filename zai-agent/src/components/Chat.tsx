import React, {useEffect, useRef} from 'react';
import ChatBlock from "./ChatBlock";
import {ChatMessage} from "@/models/ChatCompletion";

interface Props {
  chatBlocks: ChatMessage[];
  onChatScroll: (isAtBottom: boolean) => void;
  allowAutoScroll: boolean;
  loading: boolean;
}

const Chat: React.FC<Props> = ({
                                 chatBlocks, onChatScroll, allowAutoScroll, loading
                               }) => {
  const chatDivRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
  }, []);

  useEffect(() => {
    // console.log("2222222", chatDivRef.current, chatDivRef.current.scrollTop, chatDivRef.current.scrollHeight);
    if (chatDivRef.current && allowAutoScroll) {
      chatDivRef.current.scrollTop = chatDivRef.current.scrollHeight;
    }
  }, [chatBlocks]);

  useEffect(() => {
    const chatContainer = chatDivRef.current;
    if (chatContainer) {
      const isAtBottom =
          chatContainer.scrollHeight - chatContainer.scrollTop ===
          chatContainer.clientHeight;

      // Initially hide the button if chat is at the bottom
      onChatScroll(isAtBottom);
    }
  }, []);

  const handleScroll = () => {
    if (chatDivRef.current) {
      const scrollThreshold = 20;
      const isAtBottom =
          chatDivRef.current.scrollHeight -
          chatDivRef.current.scrollTop <=
          chatDivRef.current.clientHeight + scrollThreshold;

      // console.log(isAtBottom, chatDivRef.current.scrollHeight, chatDivRef.current.scrollTop, chatDivRef.current.clientHeight)
      // Notify parent component about the auto-scroll status
      onChatScroll(isAtBottom);

      // Disable auto-scroll if the user scrolls up
      if (!isAtBottom) {
        onChatScroll(false);
      }
    }
  };

  function onTyping() {
    if (chatDivRef.current && allowAutoScroll) {
      chatDivRef.current.scrollTop = chatDivRef.current.scrollHeight;
    }
  }

  return (
      <div ref={chatDivRef} className="w-full relative flex-1 overflow-auto px-4" onScroll={handleScroll}>
        <div className="relative chat-container1 flex flex-col items-center text-sm">
          {chatBlocks.map((block, index) => (
              <ChatBlock key={`chat-block-${block.id}`}
                         block={block}
                         loading={index === chatBlocks.length - 1 && loading}
                         isLastBlock={index === chatBlocks.length - 1}
                         onTyping={onTyping}/>
          ))}
          <div className="w-full h-2 flex-shrink-0"></div>
        </div>
      </div>
  );
};

export default Chat;
