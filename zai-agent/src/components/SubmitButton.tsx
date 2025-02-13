import React from 'react';
import {Image} from "primereact/image";
import sendIcon from "@/assets/ic_send.svg";

interface SubmitButtonProps {
    loading: boolean;
    disabled: boolean;
    name?: string;
}

export const SubmitButton: React.FC<SubmitButtonProps> = ({ loading, disabled, name }) => {

    return (
        <button
            name={name}
            type="submit"
            disabled={loading || disabled}
            className="mr-4 rounded-md text-primary2 disabled:opacity-40 relative z-10"
        >
            <Image src={ sendIcon as string } />
        </button>
    );
};
