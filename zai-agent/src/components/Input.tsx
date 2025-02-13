import { ChangeEvent } from "react";

interface Props {
    value?: string;
    placeholder?: string;
    onChange: (value: string) => void;
}

const Input: React.FC<Props> = ({ value, placeholder, onChange }) => {

    const [inputValue, setInputValue] = useState('');

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value);
        if (onChange) {
            onChange(event.target.value);
        }
    };

    return (
        <div className="flex flex-row w-full h-12 px-4 border border-solid border-rounded-xl border-white-2 color-app-2">
            <input className="w-full text-white text-sm" type="text"
                value={value !== undefined ? value : inputValue}
                placeholder={placeholder !== undefined ? placeholder : ''} onChange={handleInputChange} />
        </div>
    );
}
export default Input
