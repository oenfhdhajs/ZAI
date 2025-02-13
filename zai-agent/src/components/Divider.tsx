import starImg from "@/assets/home/star.svg"
import styled from "styled-components";

interface Props {
}


const Line = styled.div`
        background-color: #D0A925;
        height: 0.5px;
        width: calc(50% - 22px);`

const Divider: React.FC<Props> = () => {

    return (
        <div className="flex flex-row pl-6 pr-6 w-full items-center">
            <Line/>
            <img src={starImg} width={16} height={16} className="ml-3.5 mr-3.5"/>
            <Line />
        </div>
    );
}
export default Divider
