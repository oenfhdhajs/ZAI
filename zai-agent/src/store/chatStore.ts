import { getHistoryList } from "@/api/ai/chiat";
import {create} from "zustand";
import { token } from "./userStore";
import {timestampToDateFormat} from "@/utils/utils.ts";
import { t } from "i18next";

interface ChatInner {
    chatList: any;
}

let tempQuestion = "";

export const getTempQuestion = () => {
    return tempQuestion;
}
export const setTempQuestion = (question: string) => {
    tempQuestion = question;
}

export const clearTempQuestion = () => {
    tempQuestion = "";
}

export const requestChatList = async () => {
    if (token() === "") {
        return;
    }
    const result = await getHistoryList();
    const serverTime = window.localStorage.getItem("server_time_key");
    if (serverTime) {
        const currentDay = timestampToDateFormat(Number(serverTime) * 1000);
        const list = [];
        if (result && result.length > 0) {
            const todayList = [];
            const previousList = [];
            for (let j = 0; j < result.length; j++) {
                const item = result[j];
                const day = timestampToDateFormat(Number(item.day) * 1000);
                if (day === currentDay) {
                    todayList.push(item);
                } else {
                    previousList.push(item);
                }
            }
            list.push({ header: t("home.today"), children: todayList });
            list.push({ header: t("home.previous"), children: previousList });
            useChatStore.setState({ chatList: list });
        }
    }

}
export const clearChatList = async () => {
    useChatStore.setState({ chatList: [] });
}

const initialState = () =>{
    return {
        chatList: [],
    }
};
export const useChatStore = create<ChatInner>(() => ({
    ...initialState()
}));