export default function NavBar() {

    // const navigate = useNavigate();

    // const open = (toPath: string) => {
    //     console.log(toPath);
    //     navigate(toPath);
    // }

    return (
        <div className="flex flex-row justify-center items-center w-full h-12 px-5 border-rounded-full border border-solid border border-ed bg-ed">
            <span className="mt-1">
                <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M9.58335 18.0208C13.9556 18.0208 17.5 14.4764 17.5 10.1042C17.5 5.73191 13.9556 2.1875 9.58335 2.1875C5.2111 2.1875 1.66669 5.73191 1.66669 10.1042C1.66669 14.4764 5.2111 18.0208 9.58335 18.0208Z" stroke="#A9A9A9" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                    <path d="M18.3334 18.8542L15.6667 16.1875" stroke="#A9A9A9" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
            </span>
            <input className="w-full text-white text-sm ml-4" type="" placeholder="Search" />
        </div>
    );
}
