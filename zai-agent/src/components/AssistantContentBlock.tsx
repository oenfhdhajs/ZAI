import React from 'react';
import AssistantTokenInfoBlock from "@/components/Block/AssistantTokenInfoBlock.tsx";
import AssistantBalanceCheckBlock from "@/components/Block/AssistantBalanceCheckBlock.tsx";
import AssistantSellBlock from './Block/AssistantSellBlock';

interface AssistantContentBlockProps {
  text: string;
  onTyping?: () => void;
}

const AssistantContentBlock: React.FC<AssistantContentBlockProps> = ({text, onTyping}) => {
  // const preformattedTextStyles: React.CSSProperties = {
  //   whiteSpace: 'pre-wrap',
  //   wordBreak: 'break-word',
  // };

  const processText = (inputText: string): JSX.Element[] => {
    const sections: JSX.Element[] = [];
    const chat = JSON.parse(inputText);
    // console.log("AssistantContentBlock", chat);
    let index = 1;
    chat.map((item: any) => {
      index++;
      if (item.action === "text") {
        if (item.text === "Thinking...") {
          sections.push(<AssistantThinkBlock />);
        } else {
          sections.push(<AssistantTextBlock key={`block-id-${index}`} text={item.text} loading={false} role='' />);
        }
      } else if(item.action === "tokenInfo") {
        const token = {
          name: item.symbol,
          img: item.imageUrl,
          des: item.name,
          text: item.text,
        }
        sections.push(<AssistantTokenInfoBlock key={`block-id-${index}`} token={token} />);
      } else if (item.action === "buyToken") {
        const token = {
          oneQuestId: item.oneQuestId,
          name: item.symbol,
          img: item.imageUrl,
          des: item.name,
          text: item.text,
          needSol: item.needSol,
          transferStatus: item.transferStatus,
          code: item.code,
        }
        sections.push(<AssistantBuyBlock key={`block-id-${index}`} token={token} />);
      } else if (item.action === "sellToken") {
        const token = {
          oneQuestId: item.oneQuestId,
          name: item.symbol,
          img: item.imageUrl,
          des: item.name,
          text: item.text,
          tokenAmount: item.tokenAmount,
          transferStatus: item.transferStatus,
          code: item.code,
        }
        sections.push(<AssistantSellBlock key={`block-id-${index}`} token={token} />);
      } else if (item.action === "balanceCheck") {
        const token = {
          symbol: item.symbol,
          userBalanceCoin: item.userBalanceCoin,
          needCoin: item.needCoin,
        }

        sections.push(<AssistantBalanceCheckBlock key={`block-id-${index}`} data={token}/>);
      } else if (item.action === "tokenDetail") {
        const token = {
          name: item.symbol,
          img: item.imageUrl,
          des: item.name,
          text: item.text,
          list: item.list,
        }
        sections.push(<AssistantTokenDetailBlock key={`block-id-${index}`} token={token} />);
      } else if (item.action === "transferConfirmation") {
        const token = {
          oneQuestId: item.oneQuestId,
          name: item.symbol,
          img: item.imageUrl,
          des: item.name,
          text: item.text,
          needAmount: item.needAmount,
          symbol: item.symbol,
          tokenAmount: item.tokenAmount,
          targetAccount: item.targetAccount,
          transferStatus: item.transferStatus,
          code: item.code,
        }
        sections.push(<AssistantTransferBlock key={`block-id-${index}`} token={token} />);
      }
    })
    
    // inputText.split(SNIPPET_MARKERS.begin).forEach((section, index) => {
    //   if (index === 0 && !section.includes(SNIPPET_MARKERS.end)) {
        
    //     return;
    //   }

    //   const endSnippetIndex = section.indexOf(SNIPPET_MARKERS.end);
    //   if (endSnippetIndex !== -1) {
    //     const snippet = section.substring(0, endSnippetIndex);
    //     sections.push(
    //         <FoldableTextSection key={`foldable-${index}`} content={snippet}/>
    //     );

    //     const remainingText = section.substring(endSnippetIndex + SNIPPET_MARKERS.end.length);
    //     if (remainingText) {
    //       sections.push(<div key={`text-after-${index}`}
    //                          style={preformattedTextStyles}>{remainingText}</div>);
    //     }
    //   } else {
    //     sections.push(<div key={`text-start-${index}`} style={preformattedTextStyles}>{section}</div>);
    //   }
    // });
    return sections;
  };

  const content = processText(text);

  console.log('hahah', text);
  return (
    <div className="w-full grid grid-cols-1 gap-4 pb-3">
      {
        content
      }
    </div>
  );
};

export default AssistantContentBlock;
