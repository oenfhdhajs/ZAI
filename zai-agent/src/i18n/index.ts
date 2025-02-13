import i18next from 'i18next'
import { initReactI18next } from "react-i18next";
import translationEn from './en'
import translationEs from './en'
import translationFr from './en'
import translationDe from './en'
import translationJa from './en'
import translationKo from './en'
import translationPt from './en'
import translationRu from './en'
import translationAr from './en'
import translationIt from './en'
import translationNl from './en'
import translationHi from './en'
import translationId from './en'
import translationTh from './en'
import translationVi from './en'
import translationFa from './en'

export const langs = {
    en: "English",
    ja: "日本語",
    ko: "한국어",
    vi: "Tiếng Việt",

    es: "Español",
    fr: "Français",
    de: "Deutsch",
    pt: "Português",
    ru: "Русский",

    ar: "العربية",
    it: "Italiano",
    nl: "Nederlands",
    hi: "हिंदी",
    id: "Bahasa Indonesia",
    th: "ภาษาไทย",
    fa: "فارسی",
};
const messages = {
    en: {
        translation: translationEn,
    },
    es: {
        translation: translationEs,
    },
    fr: {
        translation: translationFr,
    },
    de: {
        translation: translationDe,
    },

    ja: {
        translation: translationJa,
    },
    ko: {
        translation: translationKo,
    },
    pt: {
        translation: translationPt,
    },
    ru: {
        translation: translationRu,
    },
    ar: {
        translation: translationAr,
    },

    it: {
        translation: translationIt,
    },
    nl: {
        translation: translationNl,
    },
    hi: {
        translation: translationHi,
    },
    id: {
        translation: translationId,
    },
    th: {
        translation: translationTh,
    },
    vi: {
        translation: translationVi,
    },
    fa: {
        translation: translationFa,
    },
};

const defaultLanguage = "en";

i18next
    .use(initReactI18next)
    .init({
        interpolation: { escapeValue: false },
        lng: getDefaultLanguage(),
        fallbackLng: defaultLanguage,
        resources: messages,
    })

export default i18next;

export function getSelectedLanguage() {
    const list = Object.entries(langs);
    for (const index in list) {
        const arr = list[index];
        // console.log("222,", arr);
        if (arr[0] === getDefaultLanguage()) {
            return arr[1];
        }
    }
    // Object.entries(langs).map(([key, item]) => {
    //     if (key === getDefaultLanguage()) {
    //         return item;
    //     }
    // });
    return "English";
}

export function getDefaultLanguage(): string {
    const lang = window.localStorage.getItem("__locale_lang__");
    if (lang) {
        return lang;
    }
    return defaultLanguage;
}

export function setLocale(locale) {
    let currentLocale = "en";
    for (const msg in messages) {
        console.log("1111,", msg);
        if (msg === locale) {
            currentLocale = msg;
        }
    }
    i18next.changeLanguage(currentLocale);
    // i18next.global.locale.value = currentLocale as I18nType.LangType;
    window.localStorage.setItem("__locale_lang__", currentLocale);
}