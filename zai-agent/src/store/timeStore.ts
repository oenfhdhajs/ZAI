import { create } from "zustand";
import { useUserStore } from "./userStore";

const TIME_KEY = '__service_time__';

interface TimeInner {
    // serverTime - localTime
    dt: number;
    activeDuration: number;
}

export const updateTime = (st: number) => {
    const nowTime = Date.now();
    useTimeStore.setState({ dt: st - nowTime });
    localStorage.setItem(TIME_KEY, JSON.stringify({ dt: st - nowTime }));
}

const MINUTE = 60 * 1000;
const HOUR = 60 * MINUTE;
const DAY = 24 * HOUR;

export const transFormTime = (leftTime: number) => {
    let result = ''
    const day = Math.floor(leftTime / DAY);

    let remainder = leftTime - day * DAY;
    const hour = Math.floor(remainder / HOUR);

    remainder = remainder - hour * HOUR;
    const min = Math.floor(remainder / MINUTE);

    remainder = remainder - min * MINUTE;
    const sec = Math.floor(remainder / 1000)
    if (day > 0) {
        result += day + "day:"
    }
    result += (hour > 9 ? hour : "0" + hour) + ":"
    result += (min > 9 ? min : "0" + min) + ":"
    result += (sec > 9 ? sec : "0" + sec)
    return result
}

export const getRemainderTime = () => {
    const { dt, activeDuration } = useTimeStore.getState();
    const { userInfo } = useUserStore.getState();
    if (!userInfo) {
        return '';
    }

    const beginTime = userInfo.beginTime;
    const endTime = userInfo.endTime;

    const st = dt + Date.now();
    if (st < beginTime || st > endTime) {
        return '';
    }
    const remainder = endTime - st;
    const res = transFormTime(remainder);

    const realDate = new Date(st);
    const newActiveDuration = 24 - realDate.getUTCHours();
    if (activeDuration != newActiveDuration) {
        useTimeStore.setState({ activeDuration: newActiveDuration });
    }
    return res;
}

export const getActiveDay = () => {
    const userInfo = useUserStore.getState().userInfo;
    const dt = useTimeStore.getState().dt;
    const st = (dt + Date.now()) / 1000;
    if (userInfo) {
        const beginTime = userInfo.beginTime;
        const endTime = userInfo.endTime;
        if (st < beginTime || st > endTime) {
            return 0;
        }
        const day = Math.ceil((st - beginTime) / 86400);
        return day;
    }
    return 0;
}
export const getActiveRatio = () => {
    const day = getActiveDay();
    if (day <= 1) {
        return 1;
    }
    return Math.pow(0.9, day);
}
const initialState = () => {
    const timeStr = localStorage.getItem(TIME_KEY);
    if (timeStr) {
        const { dt } = JSON.parse(timeStr);
        useTimeStore.setState({ dt });
    }

    return {
        dt: 0,
        activeDuration: 0,
    }
};
export const useTimeStore = create<TimeInner>(() => ({
    ...initialState()
}));