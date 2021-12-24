import util.MD5Util;

public class TestMD5 {
    public static void main(String[] args) {
        String data = "HelloWorld";
        String md5Data = MD5Util.toMD5(data);
        System.out.println(String.format("内容%s的MD5结果为%s", data, md5Data));
    }
}
