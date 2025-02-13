import DateCountDown from "@/components/DateCountDown.tsx";
import {useUserStore} from "@/store/userStore.ts";
import {formatNumberWithSpaces} from "@/utils/utils.ts";

const HomeBanner = () => {
    const userInfo = useUserStore(state => state.userInfo)
    return (
        <>
        <div className="flex flex-row justify-between items-center mt-4 w-full">
                <DateCountDown/>
            </div>

            <div className="flex flex-row justify-between items-center mt-4 w-full">
                <div className="flex flex-col justify-center items-center flex-grow">
                    <div className="font-size-3.75 text-app-black">Total participants</div>
                    <div className="font-barlow font-size-6 text-app-black mt-2">{formatNumberWithSpaces(userInfo?.totalUserNum)}</div>
                </div>
                <div className="flex flex-col justify-center items-center flex-grow">
                    <div className="font-size-3.75 text-app-black">Distribution ratio</div>
                    <div className="font-barlow font-size-6 text-app-black mt-2">{userInfo?.progress}%</div>
                </div>
            </div>

            <div className="w-full mt-3"><HomeLine /></div>

            <div className="mt-3"><UserAirdropPoints /></div>

            <div className="w-full mt-2"><HomeLine /></div>
        </>
    )
}

export default HomeBanner;

