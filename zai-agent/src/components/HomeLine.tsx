
import starSvg from '@/assets/home/home_star.svg';


interface Props {
}

const HomeLine: React.FC<Props> = () => {

    return (
        <div className='w-full flex flex-row justify-center items-center'>
            <div className='mr-3.5 flex-grow h-0.125 bg-[#7B6002]'></div>
            <img src={starSvg} />
            <div className='ml-3.5 flex-grow h-0.125 bg-[#7B6002]'></div>
        </div>
    );
}
export default HomeLine





