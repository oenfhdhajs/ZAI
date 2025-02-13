import ReactMarkdown from "react-markdown";
import rehypeRaw from 'rehype-raw'


export default function Exchange() {
  const text = "#### About $ZAI\nToken: $ZAI (Solana) <br>Contract: 8vwqxHGz1H4XxyKajr99Yxm65HjYNVtqtCNoM2yWb13e \n\nZAI simplifies Web3 for everyone—start exploring today!";

  return (
    <main className="h-full w-full bg-white">
      <div className='prose'>
        <ReactMarkdown
          rehypePlugins={[rehypeRaw]}
        >
          {text}
        </ReactMarkdown>
      </div>
    </main>
  );
}
