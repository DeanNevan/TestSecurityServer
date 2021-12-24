import util.AESUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class TestAES {
    public static void main(String[] args) {
        String data = "HelloWorld";
        String password = "abcdefghijklmnop";
        String iv = "1111111111111111";

        AESUtil.keyBytes = password.getBytes(StandardCharsets.UTF_8);
        AESUtil.ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedData = AESUtil.encryptBase64(data.getBytes(StandardCharsets.UTF_8));
        System.out.println(
                String.format(
                        "内容%s经过AES-128-CBC加密后（密码%s，iv%s）结果为%s",
                        data,
                        password,
                        iv,
                        new String(encryptedData)
                        )
        );

        byte[] decryptedData = AESUtil.decryptBase64(encryptedData);
        System.out.println(
                String.format(
                        "内容%s经过AES-128-CBC加密后（密码%s，iv%s）结果为%s",
                        new String(encryptedData),
                        password,
                        iv,
                        new String(decryptedData)
                )
        );
//        System.out.println(new String(decryptedData));
//        System.out.println(new String(decryptedDataBase64));


    }
}
