import React, { useState, useEffect } from 'react';

interface Props {
    // eslint-disable-next-line @typescript-eslint/ban-types
    text: string;
    speed: number;
    onTyping?: () => void;
  }

const Typewriter: React.FC<Props> = ({text, speed = 100, onTyping }) => {

  const [displayText, setDisplayText] = useState('');
  const [index, setIndex] = useState(0);
  const [showCursor, setShowCursor] = useState(true);

  useEffect(() => {
    // console.log(text);
    if (index < text.length) {
      const timer = setTimeout(() => {
        setDisplayText((prev) => prev + text.charAt(index));
        setIndex((prev) => prev + 1);
        setShowCursor((prev) => !prev);
      }, speed);

      if (onTyping) {
        onTyping();
      }
      return () => clearTimeout(timer);
    } else {
      if (onTyping) {
        onTyping();
      }
      setShowCursor(false);
      return () => {};
    }
  }, [index, text, speed]);

  return (
    <span className="text-primary2 text-4 leading-normal">
      {displayText}
      {showCursor && <span style={{ borderRight: '2px solid black' }}></span>}
    </span>
  );
};

export default Typewriter;