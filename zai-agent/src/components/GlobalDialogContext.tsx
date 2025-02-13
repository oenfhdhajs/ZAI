import React, { createContext, useContext, useState, ReactNode } from 'react';
import { Dialog } from 'primereact/dialog';
import SelectWalletDialog from "@/components/SelectWalletDialog.tsx";

interface DialogContextType {
    showDialog: ({ content }: { content: ReactNode }) => void;
    hideDialog: () => void;
    showSelectWalletDialog: () => void;
}

const DialogContext = createContext<DialogContextType | undefined>(undefined);

export const useDialog = (): DialogContextType => {
    const context = useContext(DialogContext);
    if (!context) {
        throw new Error('useDialog must be used within a DialogProvider');
    }
    return context;
};

export const DialogProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [dialogData, setDialogData] = useState({
        visible: false,
        content: null,
    });

    const showDialog = ({ content }: { content: ReactNode }) => {
        setDialogData({ visible: true, content });
    };

    const hideDialog = () => {
        setDialogData({ visible: false, content: null });
    };

    const showSelectWalletDialog = () => {
        showDialog({
            content: (
                <SelectWalletDialog onConfirm={hideDialog} onCancel={hideDialog} />
            ),
        });
    }

    return (
        <DialogContext.Provider value={{ showDialog, hideDialog, showSelectWalletDialog }}>
            {children}
            <Dialog
                contentClassName="bg-transparent p-0"
                showHeader={false}
                visible={dialogData.visible}
                onHide={hideDialog}
                modal
            >
                {dialogData.content}
            </Dialog>
        </DialogContext.Provider>
    );
};

