import CryptoJS from 'crypto-js';
import {t} from "i18next";
import {showToast, TOAST_TIME} from "@/store/toastStore.ts";
import {isLogin, loginToService, logout} from "@/store/userStore.ts";
import {getSignNonce} from "@/api/user";
import {signMessageWithWallet} from "@/utils/walletUtil.ts";
import { Wallet } from '@solana/wallet-adapter-react';
import { PublicKey } from '@solana/web3.js';

export const isDebugEnvironment = () => {
  return import.meta.env.MODE !== "production";
};
export const getUrlParam = (name: string) => {
  const query = new URLSearchParams(window.location.search);
  return query.get(name);
};

export const md5 = (value: string) => {
  const wordArray = CryptoJS.enc.Utf8.parse(value);
  const md5WordArray = CryptoJS.MD5(wordArray);
  return CryptoJS.enc.Hex.stringify(md5WordArray);

}

export function resizeViewPort() {
  const overflow = 10;
  document.body.style.overflowY = "hidden";
  document.body.style.marginTop = `${overflow}px`;
  document.body.style.height = window.innerHeight + overflow + "px";
  document.body.style.paddingBottom = `${overflow}px`;
  document.documentElement.scrollTo(0, overflow);
}

export const formatNumberWithSpaces = (num: any) => {
  return num?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

export const formatPriceToString = (num: number) => {
  if (num >= 1e12) {
    return formatLargeNumber(num, 1e12, 'T');
  } else if (num >= 1e9) {
    return formatLargeNumber(num, 1e9, 'B');
  } else if (num >= 1e6) {
    return formatLargeNumber(num, 1e6, 'M');
  } else if (num >= 1e3) {
    return formatLargeNumber(num, 1e3, 'K');
  } else {
    return formatNumberWithSpaces(num);
  }
}

export const formatPriceToString9 = (num?: number) => {
  if (!num) {
    return 0;
  }
  if (num >= 1e15) {
    return formatLargeNumber(num, 1e12, 'T', 1);
  } else if (num >= 1e12) {
    return formatLargeNumber(num, 1e9, 'B', 1);
  } else if (num >= 1e9) {
    return formatLargeNumber(num, 1e6, 'M', 1);
  } else if (num >= 1e6) {
    return formatLargeNumber(num, 1e3, 'K', 1);
  } else {
    return formatNumberWithSpaces(num);
  }
}

const formatLargeNumber = (num: any, divisor: any, suffix: any, digits = 2) => {
  let formatted = (num / divisor).toFixed(digits);
  formatted += suffix;
  formatted = formatNumberWithSpaces(formatted)
  return formatted;
}

export const copyText = (text: string) => {
  try {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard
          .writeText(text)
          .then(() => {
          })
          .catch(() => {
            fallbackCopyText(text);
          });
    } else {
      fallbackCopyText(text);
    }
    showToast("success", t('copy_success'), 2000)
  } catch (err) {
    fallbackCopyText(text);
    showToast("success", t('copy_success'), 2000)
    console.error("Failed to copy text: ", err);
  }
};

const fallbackCopyText = (text: string) => {
  const textArea = document.createElement("textarea");
  textArea.value = text;
  textArea.style.position = "fixed";
  textArea.style.opacity = "0";
  textArea.style.left = "-999999px";
  textArea.style.top = "-999999px";
  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();
  document.execCommand("copy");
  document.body.removeChild(textArea);
};

export const delay = async (time: number) => {
  return new Promise(resolve => setTimeout(resolve, time));
}

let lastCallbackTime: number = 0;
let callDeferred: number | null = null;
let accumulatedContent: string[] = []; // To accumulate content between debounced calls

const CHAT_STREAM_DEBOUNCE_TIME = 250;

export const debounceCallback = (callback: (content: string[]) => void, delay: number = CHAT_STREAM_DEBOUNCE_TIME) => {
  return (content: string[]) => {
    accumulatedContent.push(...content); // Accumulate content on each call
    const now = Date.now();
    const timeSinceLastCall = now - lastCallbackTime;

    if (callDeferred !== null) {
      clearTimeout(callDeferred);
    }

    callDeferred = window.setTimeout(() => {
      callback(accumulatedContent); // Pass the accumulated content to the original callback
      lastCallbackTime = Date.now();
      accumulatedContent = []; // Reset the accumulated content after the callback is called
    }, delay - timeSinceLastCall < 0 ? 0 : delay - timeSinceLastCall);  // Ensure non-negative delay

    lastCallbackTime = timeSinceLastCall < delay ? lastCallbackTime : now; // Update last callback time if not within delay
  };
}

export const sleep = (ms: number) => {
  return new Promise(resolve => setTimeout(resolve, ms));
}

export const checkSignMessage = (wallet: Wallet | null, connected: boolean, publicKey: PublicKey | null, signMessage: ((message: Uint8Array) => Promise<Uint8Array>) | undefined, disconnect: any) => {
  if (wallet && connected && !isLogin()) {
    getSignNonce({ walletAddress: publicKey?.toBase58() }).then(async (res) => {
      if (res) {
        try {
          const signedMessage = await signMessageWithWallet(signMessage, res.uId);
          console.log("signed message:", signedMessage);
          if (!publicKey) return;
          await loginToService(publicKey.toBase58(), signedMessage);
          showToast("success", t("home.connect_success"), TOAST_TIME);
        } catch (error) {
          console.error('sign error:', error);
          disconnect();
          window.localStorage.clear();
          logout();
        }
      }
    });
  }
}

export function timestampToDateFormat(timestamp: any) {
  const dateObj = new Date(timestamp);
  const formattedDate = dateObj.toISOString().slice(0, 10);
  const [year, month, day] = formattedDate.split("-");
  return `${year}-${month}-${day}`;
}

export const handleTransactionFailedToast = (code: number) => {
  let errorMsg = "";
  switch (code) {
    case 491:
      errorMsg = t("home.transaction_failed_toast1");
      break;
    case 504:
      errorMsg = t("home.transaction_failed_toast2");
      break;
    case 471:
      errorMsg = t("home.transaction_failed_toast3");
      break;
    case 461:
      errorMsg = t("home.transaction_failed_toast4");
      break;
    case 500:
      errorMsg = t("home.transaction_failed_toast5");
      break;
    case 400:
      errorMsg = t("home.transaction_failed_toast6");
      break;
    case 472:
      errorMsg = t("home.sufficient_transaction");
      break;
    default:
      errorMsg = t("home.try_again_content");
  }
  return errorMsg;
};