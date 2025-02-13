
import { Skeleton } from 'primereact/skeleton';


interface Props {
    src: string;
    width?: number;
}

const ImageWithSkeleton: React.FC<Props> = ({ src }) => {

    const [ loadCompleted, setLoadCompleted] = useState(false);

    useEffect(() => {
        const img = new Image();
        img.onload = () => {
            console.log('img onload');
            setLoadCompleted(true);
        };
        img.onerror = () => {};
        img.src = src;
    }, [src]);

    if (!loadCompleted) {
        return (
            <div className='mx-4 mt-12 rounded-2xl'><Skeleton width="80px" height="80px" className="bg-gray-400"></Skeleton></div>
        );
    }
    return (
        <img className="mx-4 mt-12 rounded-2xl relative" src={src} width={80} height={80} onLoad={ () => {setLoadCompleted(true)} } />
    );
}
export default ImageWithSkeleton





