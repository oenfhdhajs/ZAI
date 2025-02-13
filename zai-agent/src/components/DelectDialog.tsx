const DelectDialog = (props: any) => {
    const {t} = useTranslation();

    const setVisible = async () => {
        props.onConfirm();
    }

    const closeDialog = () => {
        props.onCancel();
    };

    return (
        <div>
            <div className="flex flex-col w-full md:w-500px items-center mt-6 relative px-8 pb-6 bg-background rounded-24px pt-6">
                <span className="text-primary2 text-xl mb-4 self-start font-bold">{ t("chat.delete_title") }</span>
                <div
                    className="flex items-center w-full"
                    onClick={() => {}}>
                    <span className="text-primary2 text-[#666666] font-400">{ t("chat.delete_desc") }</span>
                </div>
                <div className="flex flex-row w-full justify-end items-end mt-5">
                    <div className="px-5 py-2.5 border border-[#E4E4E7] rounded-xl hover:bg-[#E4E4E780]" onClick={closeDialog}>{ t("chat.delete_cancel") }</div>
                    <div className="px-5 py-2.5 bg-[#EA2EFE] text-white rounded-xl ml-4 hover:bg-[#EA2EFE80]" onClick={setVisible}>{ t("chat.delete_continue") }</div>
                </div>
            </div>
        </div>
    );
};

export default DelectDialog;