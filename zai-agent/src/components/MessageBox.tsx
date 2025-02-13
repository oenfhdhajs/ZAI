import { ChangeEvent, FormEvent } from "react";
import {Image} from "primereact/image";
import pauseIcon from "@/assets/ic_pause.svg";

const MAX_ROWS = 20;

interface MessageBoxProps {
    // eslint-disable-next-line @typescript-eslint/ban-types
    callApp: Function;
    loading: boolean;
    setLoading: (loading: boolean) => void;
    allowImageAttachment: string;
  }
  
  // Methods exposed to clients using useRef<MessageBoxHandles>
  export interface MessageBoxHandles {
    clearInputValue: () => void;
    getTextValue: () => string;
    reset: () => void;
    resizeTextArea: () => void;
    focusTextarea: () => void;
    pasteText: (text: string) => void;
    askQuickQuestion: (text: string) => void;
  }
  

const MessageBox = forwardRef<MessageBoxHandles, MessageBoxProps>(({loading, setLoading, callApp, allowImageAttachment}, ref) => {
    const {t} = useTranslation();
    const textValue = useRef('');
    const [isTextEmpty, setIsTextEmpty] = useState(true);
    const textAreaRef = useRef<HTMLTextAreaElement>(null);
    const resizeTimeoutRef = useRef<number | null>(null);
    const [isComposing, setComposing] = useState(false);
    

    const setTextValue = (value: string) => {
      textValue.current = value;
    }

    const setTextAreaValue = (value: string) => {
      if (textAreaRef.current) {
        textAreaRef.current.value = value;
      }
      setIsTextEmpty(textAreaRef.current?.value.trim() === '');
      debouncedResize();
    }

    useImperativeHandle(ref, () => ({
      // Method to clear the textarea
      clearInputValue: () => {
        clearValueAndUndoHistory(textAreaRef);
      },
      getTextValue: () => {
        return textValue.current;
      },
      reset: () => {
        clearValueAndUndoHistory(textAreaRef);
        setTextValue('');
        setTextAreaValue('');
      },
      resizeTextArea: () => {
        if (textAreaRef.current) {
          textAreaRef.current.style.height = 'auto';
        }
      },
      focusTextarea: () => {
        if (textAreaRef.current) {
          textAreaRef.current.focus();
        }
      },
      pasteText: (text: string) => {
        insertTextAtCursorPosition(text);
      },
      askQuickQuestion: (text: string) => {
          setTextValue("");
          setTextAreaValue("");
          insertTextAtCursorPosition(text);
          if (textAreaRef.current) {
              setTextValue(textAreaRef.current.value);
          }
          callApp(textAreaRef.current?.value || '', []);
          if (textAreaRef.current) {
              textAreaRef.current.style.height = 'auto';
          }
      },
    }));

    // Function to handle auto-resizing of the textarea
    const handleAutoResize = useCallback(() => {
      if (textAreaRef.current) {
        const target = textAreaRef.current;
        const maxHeight = parseInt(getComputedStyle(target).lineHeight || '0', 10) * MAX_ROWS;

        target.style.height = 'auto';
        if (target.scrollHeight <= maxHeight) {
          target.style.height = `${target.scrollHeight}px`;
        } else {
          target.style.height = `${maxHeight}px`;
        }
      }
    }, []);

    // Debounced resize function
    const debouncedResize = useCallback(() => {
      if (resizeTimeoutRef.current !== null) {
        clearTimeout(resizeTimeoutRef.current);
      }
      resizeTimeoutRef.current = window.setTimeout(() => {
        handleAutoResize();
      }, 100); // Adjust the debounce time as needed
    }, []);

    const handleTextValueUpdated = () => {
      debouncedResize();

      // After resizing, scroll the textarea to the insertion point (end of the pasted text).
      if (textAreaRef.current) {
        const textarea = textAreaRef.current;
        // Check if the pasted content goes beyond the max height (overflow scenario)
        if (textarea.scrollHeight > textarea.clientHeight) {
          // Scroll to the bottom of the textarea
          textarea.scrollTop = textarea.scrollHeight;
        }
      }
    };

    function clearValueAndUndoHistory(textAreaRef: React.RefObject<HTMLTextAreaElement>) {
      setTextValue('');
      setTextAreaValue('');
    }

    const insertTextAtCursorPosition = (textToInsert: string) => {
      if (textAreaRef.current) {
        const textArea = textAreaRef.current;
        const startPos = textArea.selectionStart || 0;
        const endPos = textArea.selectionEnd || 0;
        const text = textArea.value;
        const newTextValue =
          text.substring(0, startPos) +
          textToInsert +
          text.substring(endPos);

        // Update the state with the new value
        setTextValue(newTextValue);
        setTextAreaValue(newTextValue);

        // Dispatch a new InputEvent for the insertion of text
        // This event should be undoable
        // const inputEvent = new InputEvent('input', {
        //   bubbles: true,
        //   cancelable: true,
        //   inputType: 'insertText',
        //   data: textToInsert,
        // });
        // textArea.dispatchEvent(inputEvent);

        // Move the cursor to the end of the inserted text
        const newCursorPos = startPos + textToInsert.length;
        setTimeout(() => {
          textArea.selectionStart = newCursorPos;
          textArea.selectionEnd = newCursorPos;
          // Scroll to the insertion point after the DOM update
          if (textArea.scrollHeight > textArea.clientHeight) {
            textArea.scrollTop = textArea.scrollHeight;
          }
        }, 0);
      }
    };

    const handlePaste = (event: React.ClipboardEvent<HTMLTextAreaElement>) => {

    //   if (event.clipboardData && event.clipboardData.items) {
        // const items = Array.from(event.clipboardData.items);

        // for (const item of items) {
        //   if (item.type.indexOf("image") === 0 && allowImageAttachment !== 'no') {
        //     event.preventDefault();
        //     const file = item.getAsFile();
        //     if (file) {
        //       const reader = new FileReader();
        //       reader.onload = (loadEvent) => {
        //         if (loadEvent.target !== null) {
        //           const base64Data = loadEvent.target.result;

        //           if (typeof base64Data === 'string') {
        //             preprocessImage(file, (base64Data, processedFile) => {
        //               setFileDataRef((prevData) => [...prevData, {
        //                 id: 0,
        //                 fileData: {
        //                   data: base64Data,
        //                   type: processedFile.type,
        //                   source: 'pasted',
        //                   filename: 'pasted-image',
        //                 }
        //               }]);
        //             });
        //             if (allowImageAttachment == 'warn') {
        //               // todo: could warn user
        //             }
        //           }
        //         }
        //       };
        //       reader.readAsDataURL(file);
        //     }
        //   } else {
        //   }
        // }
    //   }

      // Get the pasted text from the clipboard
    //   const pastedText = event.clipboardData.getData('text/plain');
    };

    const handleCompositionStart = () => {
      setComposing(true);
    }

    const handleCompositionEnd = () => {
      setComposing(false);
    }

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const checkForSpecialKey = (e: any) => {
      const isEnter = (e.key === 'Enter' && !isComposing);

      if (isEnter) {
        if (e.shiftKey) {
          return;
        } else {
          if (!loading) {
            e.preventDefault();
            if (textAreaRef.current) {
              setTextValue(textAreaRef.current.value);
            }
            callApp(textAreaRef.current?.value || '', []);
          }
        }
      }
    };

    const handleTextChange = (event: ChangeEvent<HTMLTextAreaElement>) => {
      setIsTextEmpty(textAreaRef.current?.value.trim() === '');
      handleTextValueUpdated();
    };

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      e.stopPropagation();
      if (textAreaRef.current) {
        setTextValue(textAreaRef.current.value);
      }
      callApp(textAreaRef.current?.value || '', []);
      if (textAreaRef.current) {
        textAreaRef.current.style.height = 'auto';
      }
    };
    
    const handleCancel = (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      event.stopPropagation();

      // ChatService.cancelStream();
      setLoading(false);
    };

    return (
      <div
        // style={{position: "sticky"}}
          style={{zIndex: '3'}}
        className="absolute bottom-0 left-0 w-full border md:border-t-0 md:border-transparent !bg-transparent pt-2 z-3">
        <form onSubmit={handleSubmit}
              className="stretch flex flex-row gap-3 last:mb-2 md:last:mb-6 mx-auto md:max-w-3xl lg:max-w-[40rem] xl:max-w-[48rem] bg-white_70"
              style={{borderRadius: "1rem", border: isTextEmpty ? "1px solid white" : "2px solid #EA2EFE"}}>
          <div id="message-box-border"
               className="relative flex flex-col h-full flex-1 w-full py-2 flex-grow md:py-3 bg-gray-850
             text-primary2"
          >
            {/* Container for Textarea and Buttons */}
            <div className="flex items-center w-full relative space-x-2">

              {/* Textarea */}
              <textarea
                id="sendMessageInput"
                name="message"
                tabIndex={0}
                ref={textAreaRef}
                rows={1}
                className="flex-auto m-0 resize-none border-0 bg-transparent px-18px py-6px text-sm focus:ring-0 focus-visible:ring-0 outline-none shadow-none placeholder-color_text_light color-black"
                placeholder={t('agent.input_placeholder')}
                onKeyDown={checkForSpecialKey}
                onCompositionStart={handleCompositionStart}
                onCompositionEnd={handleCompositionEnd}
                onChange={handleTextChange}
                onPaste={handlePaste}
                style={{ minWidth: 0 }}
              ></textarea>

              {/* Cancel/Submit Button */}
              <div className="flex items-center justify-end">
                {
                    loading ? (
                        <button
                          onClick={(e) => handleCancel(e)}
                          className="mr-4 text-primary2">
                            <Image src={pauseIcon as string} />
                        </button>
                    ): <SubmitButton
                        disabled={isTextEmpty || loading}
                        loading={loading}
                    />
                }
              </div>
            </div>
          </div>
        </form>
      </div>
    );
  });

export default MessageBox;