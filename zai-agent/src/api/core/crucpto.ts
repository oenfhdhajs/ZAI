import CryptoJS from "crypto-js";
import JSEncrypt from "jsencrypt";
import jsrsasign from "jsrsasign";

const rsaPublicKey = ``;
const rsaPrivateKey = ``;

export const rsaEncode = (aesKey: string) => {
  const jsencrypt = new JSEncrypt();
  jsencrypt.setPublicKey(rsaPublicKey);
  return jsencrypt.encrypt(aesKey);
};

export const generateSign = (data: string) => {
  // rsa sign
  const md5 = CryptoJS.enc.Hex.stringify(CryptoJS.MD5(CryptoJS.enc.Utf8.parse(data)));
  const rsa = jsrsasign.KEYUTIL.getKey(rsaPrivateKey);
  const sig = new jsrsasign.KJUR.crypto.Signature({ alg: "SHA1withRSA" });
  sig.init(rsa);
  sig.updateString(md5);
  return jsrsasign.hextob64(sig.sign());
};

export const aesEncode = (keyValue: string, content: string) => {
  const key = CryptoJS.enc.Utf8.parse(keyValue);
  const iv = CryptoJS.enc.Utf8.parse(keyValue);
  return CryptoJS.AES.encrypt(content, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  }).toString();
};

export const aesDecrypted = (keyValue: string, data: string) => {
  const key = CryptoJS.enc.Utf8.parse(keyValue);
  const iv = CryptoJS.enc.Utf8.parse(keyValue);
  return CryptoJS.AES.decrypt(data, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  }).toString(CryptoJS.enc.Utf8);
};

export const generateRandom = (length: number) => {
  let result = "";
  const characters = "abcdefghijklmnopqrstuvwxyz0123456789";
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
};
export const getRandomCharacter = () => {
  let result = "";
  const characters = "abcdefghijklmnopqrstuvwxyz";
  for (let i = 0; i < 1; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
};
