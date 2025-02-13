
import homeLogo from '@/assets/home/home_logo.svg'

interface Props {
    width: number;
    height: number;
    className?: string;
}

const Logo: React.FC<Props> = ({ width, height, className }) => {

    return (
        <img src={homeLogo} width={width} height={height} className={className}/>
    );
}
export default Logo
