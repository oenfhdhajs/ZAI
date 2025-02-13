import styled from "styled-components";
import loadingImg from "@/assets/loading_dot.svg";

interface Props {
    width: number;
    height: number;
}


const Loading = styled.div`
        position: relative;
        width: 100%;
        height: 100%;
    `
const Img = styled.div`
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%,-50%);
    `
const RankItem: React.FC<Props> = ({ width, height}) => {

    return (
        <Loading><Img>
            <img src={loadingImg} width={width} height={height} className="loading_rotate"/>
        </Img></Loading>
    );
}
export default RankItem