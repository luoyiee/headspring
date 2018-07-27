package cc.xiaojiang.liangbo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * author zoush
 * CreateTime 2018-07-24 16:02
 */
public class SignUtils {

    private static final String SECRET = "SEgX1EdNOBilB3H58ToLHY9QSjxZpvC9";

    public static String sign(Map<String,String> map) {
        try {
            TreeMap<String,Object> treeMap = splitMap(map);
            treeMap.remove("sign");
            treeMap.put("secret",SECRET);
            return SHA1(mapToString(treeMap));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String postSign(Object object){
        try{
            TreeMap<String,Object> treemap = getFiledsMap(object);
            treemap.remove("sign");
            treemap.put("secret",SECRET);
            return SHA1(mapToString(treemap));
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }
    /**
     * 获取属性名(name)，属性值(value)组成的map
     * */
    private static TreeMap getFiledsMap(Object o){
        Field[] fields = o.getClass().getDeclaredFields();
        TreeMap infoMap = new TreeMap();
        for (int i=0;i<fields.length;i++){
            infoMap.put(fields[i].getName(),getFieldValueByName(fields[i].getName(),o));
        }

        return infoMap;
    }

    /**
     * 根据属性名获取属性值
     * */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }


    private static TreeMap<String,Object> splitMap(Map<String,String> map){

        TreeMap<String,Object> treeMap = new TreeMap<>();

        for (Map.Entry<String,String> entry:map.entrySet()){
            Object val =  entry.getValue();
            treeMap.put(entry.getKey(),val);
        }

        return treeMap;
    }



    private static String mapToString(TreeMap<String, Object> map) {
        StringBuilder unsignedBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            unsignedBuilder.append(entry.getKey());
            unsignedBuilder.append("=");
            unsignedBuilder.append(entry.getValue());
            unsignedBuilder.append("&");
        }
        String unsignedStr = unsignedBuilder.substring(0, unsignedBuilder.length() - 1);
        return unsignedStr;
    }


    /**
     * SHA1 安全加密算法
     */
    private static String SHA1(String unSignStr) throws DigestException, NoSuchAlgorithmException {
        //指定sha1算法
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(unSignStr.getBytes());
        //获取字节数组
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        // 字节数组转换为 十六进制 数
        for (byte aMessageDigest : messageDigest) {
            String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString().toUpperCase();
    }
}
