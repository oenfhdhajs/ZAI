import React, {useEffect} from 'react';
import {Image} from "primereact/image";
import girlIcon from "@/assets/ic_zai_girl.webp";
import arrowGuideIcon from "@/assets/ic_arrow_dash.svg";
import {t} from "i18next";

interface Props {

}

const SendGuide: React.FC<Props> = () => {

  useEffect(() => {
  }, []);

  return (
      <div className="flex flex-col items-center">
          <Image className="w-16" src={girlIcon as string} />
          <div className="text-6 font-semibold color-black leading-normal mt-4">{t("chat.guide_title")}</div>
          <div className="text-3.5 font-normal text-color_text_light leading-normal mt-2 text-center">{t("chat.guide_des")}</div>
          <Image className="w-25 mt-5 md:mt-14" src={arrowGuideIcon as string} />
      </div>
  );
};

export default SendGuide;
