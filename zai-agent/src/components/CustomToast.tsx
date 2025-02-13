import {Toast, ToastMessage} from "primereact/toast";
import doneIcon from "@/assets/toast_done.png"
import failIcon from "@/assets/toast_fail.png"
import React, {useEffect} from "react";
import {useToastStore} from "@/store/toastStore.ts";

interface Props {
}

const CustomToast: React.FC<Props> = () => {
    const toast = useRef<Toast>(null)
    const toastShowing = useToastStore(state => state.showing)
    const toastData = useToastStore(state => state.toast)

    function showToast(){
        if (toast.current) {
            toast.current.show({severity: toastData?.severity, detail: toastData?.detail, life: toastData?.life, closable: false,
                style:{
                    marginLeft: '40px',
                    backgroundColor: "rgba(32, 22, 20, 0.8)",
                    borderRadius: "35px",
                    color: "white",
                    padding: "0 8px"
                },
                content: (prop: any) => (
                    <div className="w-full flex flex-row items-center"><img src={toastData?.severity === 'success' ? doneIcon : failIcon} width={18} height={18}/>
                        <div className="ml-2.5">{prop.message.detail}</div>
                    </div>
                )
            } as unknown as ToastMessage)
        }
    }

    useEffect(() => {
        if (toastShowing) {
            showToast()
            useToastStore.setState?.({showing: false})
        }
    }, [toastShowing]);
    return (
        <Toast ref={toast}/>
    );
}
export default CustomToast
