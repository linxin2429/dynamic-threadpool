package cn.xldeng.common.toolkit;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.model.PoolParameter;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 22:41
 */
public class Md5Util {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private static final ThreadLocal<MessageDigest> MESSAGE_DIGEST_LOCAL = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    });

    public static String md5Hex(byte[] bytes) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MESSAGE_DIGEST_LOCAL.get();
            if (messageDigest != null) {
                return encodeHexString(messageDigest.digest(bytes));
            }
            throw new NoSuchAlgorithmException("MessageDigest get MD5 instance error");
        } finally {
            MESSAGE_DIGEST_LOCAL.remove();
        }
    }

    public static String md5Hex(String value, String encode) {
        try {
            return md5Hex(value.getBytes(encode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeHexString(byte[] bytes) {
        int l = bytes.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & bytes[i]) >>> 4];
            out[j++] = DIGITS_LOWER[(0x0F & bytes[i])];
        }
        return new String(out);
    }

    public static String getTpContentMd5(PoolParameter config) {
        return Md5Util.md5Hex(ContentUtil.getPoolContent(config), "UTF-8");
    }

    public static String compareMd5ResultString(List<String> changedGroupKeys) throws UnsupportedEncodingException {
        if (null == changedGroupKeys) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String groupKey : changedGroupKeys) {
            String[] dataIdGroupId = GroupKey.parseKey(groupKey);
            sb.append(dataIdGroupId[0]);
            sb.append(Constants.WORD_SEPARATOR);
            sb.append(dataIdGroupId[1]);
            if (dataIdGroupId.length == 3) {
                if (!StringUtils.isEmpty(dataIdGroupId[2])) {
                    sb.append(Constants.WORD_SEPARATOR);
                    sb.append(dataIdGroupId[2]);
                }
            }
            sb.append(Constants.LINE_SEPARATOR);
        }
        return URLEncoder.encode(sb.toString(), "UTF-8");
    }

}