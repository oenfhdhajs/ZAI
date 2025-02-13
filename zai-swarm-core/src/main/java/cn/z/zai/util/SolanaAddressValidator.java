package cn.z.zai.util;

import cn.hutool.core.codec.Base58;


public class SolanaAddressValidator {


    public static boolean isValidSolanaAddress(String address) {


        if (address == null) {
            return false;
        }

        if (!address.matches("^[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]+$")) {
            return false;
        }

        try {

            byte[] decoded = Base58.decode(address);

            return decoded.length == 32;
        } catch (IllegalArgumentException e) {

            return false;
        }
    }

}
