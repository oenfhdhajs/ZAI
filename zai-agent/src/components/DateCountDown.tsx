import {useEffect} from "react";
import {localDate, useUserStore} from "@/store/userStore.ts";
import { getRemainderTime } from "@/store/timeStore";

interface Props {
}

const DateCountDown: React.FC<Props> = () => {
    const userInfo = useUserStore((state)=>state.userInfo)
    const [leftTime, setLeftTime] = useState('')

    useEffect(() => {
        setLeftTime(getRemainderTime())

        const timerId = setInterval(()=>{
            setLeftTime(getRemainderTime())
        },1000)
        return ()=>{
            clearInterval(timerId);
        }
    }, []);

    return (
        <div className={`text-red-5 font-barlow font-500 font-size-4.25 ${localDate() == 0 || !userInfo || leftTime === ''?'invisible':'visible'}`}>{leftTime}</div>
    );
}
export default DateCountDown