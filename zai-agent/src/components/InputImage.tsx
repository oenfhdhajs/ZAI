interface Props {
}

const InputImage: React.FC<Props> = () => {
    const fileInputRef = useRef<HTMLInputElement>(null);
    const [dataUrl, setDataUrl] = useState('');


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const files = event.target.files;
        if (files && files.length > 0) {
            const file = files[0];
            console.log(file);
            const reader = new FileReader();
            reader.onload = (e) => {
                const dataUrl = e.target?.result as string;
                setDataUrl(dataUrl);
                console.log(dataUrl);
            };
            reader.readAsDataURL(file);
        }
    };

    const handleClick = (_: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        fileInputRef?.current?.click();
    };

    return (
        <div className="flex flex-row justify-center" onClick={handleClick}>

            <input type="file" className="hidden" ref={fileInputRef} onChange={handleChange} />
            {
                dataUrl === '' ? <div className="bg-ed rounded-full h-24 w-24 flex flex-row justify-center items-center">
                    <span>
                        <svg width="44" height="44" viewBox="0 0 44 44" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path fillRule="evenodd" clipRule="evenodd" d="M31.4141 8.35642H37.5927C39.7366 8.35642 41.4908 10.1106 41.4908 12.2546V35.6436C41.4908 37.7876 39.7366 39.5417 37.5927 39.5417H6.40732C4.26333 39.5417 2.50916 37.7876 2.50916 35.6436V12.2546C2.50916 10.1106 4.26333 8.35642 6.40732 8.35642H12.5859L14.9833 5.72516C15.7239 4.92603 16.7764 4.45825 17.8679 4.45825H26.132C26.6715 4.45858 27.2049 4.57143 27.6982 4.78957C28.1916 5.00771 28.634 5.32636 28.9972 5.72516L31.4141 8.35642ZM12.2546 23.9491C12.2546 29.3286 16.6205 33.6945 22 33.6945C27.3795 33.6945 31.7454 29.3286 31.7454 23.9491C31.7454 18.5696 27.3795 14.2037 22 14.2037C16.6205 14.2037 12.2546 18.5696 12.2546 23.9491ZM27.8472 23.9491C27.8472 27.1784 25.2293 29.7963 22 29.7963C18.7706 29.7963 16.1527 27.1784 16.1527 23.9491C16.1527 20.7197 18.7706 18.1018 22 18.1018C25.2293 18.1018 27.8472 20.7197 27.8472 23.9491ZM31.3538 12.575C30.8015 12.575 30.3538 13.0227 30.3538 13.575C30.3538 14.1272 30.8015 14.575 31.3538 14.575H34.0775C34.6298 14.575 35.0775 14.1272 35.0775 13.575C35.0775 13.0227 34.6298 12.575 34.0775 12.575H31.3538Z" fill="#2AABEE" />
                        </svg>
                    </span></div> :
                    <img src={dataUrl} alt="Avatar" className="w-24 h-24 rounded-full" />
            }
        </div>
    );
}
export default InputImage
