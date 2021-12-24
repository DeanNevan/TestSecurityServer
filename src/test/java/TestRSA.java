import util.RSAUtil;

import java.security.KeyPair;


public class TestRSA {
    public static void main(String[] args) {
        try {
            String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                    "MIIBPgIBAAJBAMvx30Fs0OpOe+/FGJWQNtxI6OuXuBLIuMeVz34570gAkCgorB97\n" +
                    "ibFZLtZjAUUK7ukAX22s2+VokBIQfgLyzgkCAwEAAQJAXpVMDxGiSigf/nEQF70M\n" +
                    "VFlT/H8elUeVuqpV0pqXWXQ6VwkdcvS42wAG3AKJ02kke0heQgXcFPJLJOwK96y5\n" +
                    "oQIjAO9U1695EiwD3QyoBZBBPx0pR+etJZrUFB/MDL5x6Eev/Y0CHwDaJhq7lHSk\n" +
                    "NVFwso6uU0NRfqJHynXcqN1ntBKOfW0CIwCt3GuHLKO31+KoRBMulUd9LrTup4ju\n" +
                    "7evkoX4Mh4EfOsktAh8AoqTSPZSbumo+RAX8tyBBCpudpmTepxwHpu/s/eupAiMA\n" +
                    "v5wWrnSz4GsQ2QTG/HA9oIb+O7wFdXxtvbH433lNdlZA/Q==\n" +
                    "-----END RSA PRIVATE KEY-----";
            String decryptedData = RSAUtil.decryptRSA(
                    "vLQrMnCXaOm4nTiBhxOdVPJXP+E1SBNZf0EF2LPaKaohR7qaWvJs2/hxwStxih8FQL03JIjhWqIbrWSsjUffyw==",
                    privateKey
            );
            System.out.println(String.format("解密后：%s", decryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
