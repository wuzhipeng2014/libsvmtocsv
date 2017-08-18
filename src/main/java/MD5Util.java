import java.security.MessageDigest;

/**
 * Created by zhipengwu on 17-8-17.
 */
public class MD5Util {
    public static void main(String[] args) {
        System.out.println(getMd5("wuzhipeng"));
    }

    /**
     * 用于获取一个String的md5值
     * @param string
     * @return
     */
    private static MessageDigest messageDigest = null;
    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getMd5(String str) {
        String uidMd5 = "";

        // 输入的字符串转换成字节数组
        byte[] inputByteArray = str.getBytes();

        // inputByteArray是输入字符串转换得到的字节数组
        messageDigest.update(inputByteArray);

        // 转换并返回结果，也是字节数组，包含16个元素
        byte[] resultByteArray = messageDigest.digest();

        for (byte o : resultByteArray) {
            uidMd5 = uidMd5 + String.format("%02X", o);
        }
        uidMd5 = uidMd5.substring(uidMd5.length() - 4);
        int uidInt = Integer.parseInt(uidMd5, 16);
        return String.valueOf(uidInt);
//        float bucket = (float) uidInt / 65535;
//        return bucket;
    }
}
