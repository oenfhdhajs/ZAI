import {create} from "zustand";
import { login, sync, User } from "@/api/user"

const FIRST_KEY = '__newton_first_launch__';
const USER_KEY = 'user_store';
const LOCAL_TIME_KEY = 'local_time';

const testToken = '';
let tokenValue = '';

export const token = () => {
    return tokenValue;
}
export const localDate = () => {
    const strDate = localStorage.getItem(LOCAL_TIME_KEY);
    if (strDate){
        return Number(strDate);
    } else {
        return 0;
    }
}

export const loginToService = async (address: string, signature: string) => {
    const userString = localStorage.getItem(USER_KEY);
    if (userString) {
        const user: User = JSON.parse(userString);
        tokenValue = user.token;
        const newUser = await sync();

        if (newUser) {
            newUser.token = tokenValue;
            useUserStore.setState({ userInfo: newUser });
            localStorage.setItem(LOCAL_TIME_KEY, Date.now()+'')
            localStorage.setItem(USER_KEY, JSON.stringify(newUser));
        }
    } else {
        let user;
        if (testToken.length == 0) {
            user = await login({address: address, signature: signature, source: 1});
        } else {
            tokenValue = testToken;
            user = await sync();
            user.token = testToken;
        }
        if (user) {
            tokenValue = user.token;
            useUserStore.setState({ userInfo: user });
            localStorage.setItem(LOCAL_TIME_KEY, Date.now()+'')
            localStorage.setItem(USER_KEY, JSON.stringify(user));
        }
    }
};

export const addUserCoin = (price: number) => {
    const user = useUserStore.getState().userInfo;
    if (user) {
        const newUser = {
            ...user,
            totalCoins: user.totalCoins + price,
        };
        console.log('addUserCoin', price, newUser);
        useUserStore.setState({ userInfo: newUser });
        localStorage.setItem(USER_KEY, JSON.stringify(newUser));
    }
}

export const isLogin = () => {
    // console.log("tttt:", useUserStore.getState().userInfo)
    return useUserStore.getState().userInfo;
}

export const pushFirstComplete = () => {
    useUserStore.setState({ first: false });
    localStorage.setItem(FIRST_KEY, "1");
}

export const pushStartLayout = () => {
    useUserStore.setState({ startLayout: true });
}
export const getFirstKeyInfo = () => {
    const isFirst = localStorage.getItem(FIRST_KEY) || "";
    return isFirst === "2";
}
export const pushSecondComplete = () => {
    useUserStore.setState({ first: false });
    localStorage.setItem(FIRST_KEY, "2");
}

export const logout = () => {
    useUserStore.setState({
        userInfo: undefined,
    });
}

interface UserInner {
    userInfo: User | undefined;
    first: boolean;
    startLayout: boolean;
}

const initialState = () =>{
    const isFirst = localStorage.getItem(FIRST_KEY) || "";
    const userString = localStorage.getItem(USER_KEY);
    if (userString) {
        const user: User = JSON.parse(userString);
        tokenValue = user.token;
        return {
            userInfo: user,
            first: isFirst.length === 0,
            startLayout: false,
        }
    }
    return {
        userInfo: undefined,
        first: isFirst.length === 0,
        startLayout: false,
    }
};
export const useUserStore = create<UserInner>(() => ({
    ...initialState()
}));