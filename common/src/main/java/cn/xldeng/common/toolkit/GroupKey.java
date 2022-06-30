package cn.xldeng.common.toolkit;

import org.springframework.util.StringUtils;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 13:58
 */
public class GroupKey {
    public static String getKey(String dataId, String group) {
        return getKey(dataId, group, "");
    }

    public static String getKey(String dataId, String group, String datumStr) {
        return doGetKey(dataId, group, datumStr);
    }

    public static String getKeyTenant(String dataId, String group, String tenant) {
        return doGetKey(dataId, group, tenant);
    }

    private static String doGetKey(String dataId, String group, String datumStr) {
        StringBuilder sb = new StringBuilder();
        urlEncode(dataId, sb);
        sb.append('+');
        urlEncode(group, sb);
        if (!StringUtils.isEmpty(datumStr)) {
            sb.append('+');
            urlEncode(datumStr, sb);
        }

        return sb.toString();
    }

    /**
     * Parse key.
     *
     * @param groupKey group key
     * @return parsed key
     */
    public static String[] parseKey(String groupKey) {
        StringBuilder sb = new StringBuilder();
        String dataId = null;
        String group = null;
        String tenant = null;

        for (int i = 0; i < groupKey.length(); ++i) {
            char c = groupKey.charAt(i);
            if ('+' == c) {
                if (null == dataId) {
                    dataId = sb.toString();
                    sb.setLength(0);
                } else if (null == group) {
                    group = sb.toString();
                    sb.setLength(0);
                } else {
                    throw new IllegalArgumentException("invalid groupkey:" + groupKey);
                }
            } else if ('%' == c) {
                char next = groupKey.charAt(++i);
                char nextnext = groupKey.charAt(++i);
                if ('2' == next && 'B' == nextnext) {
                    sb.append('+');
                } else if ('2' == next && '5' == nextnext) {
                    sb.append('%');
                } else {
                    throw new IllegalArgumentException("invalid groupkey:" + groupKey);
                }
            } else {
                sb.append(c);
            }
        }

        if (StringUtils.isEmpty(group)) {
            group = sb.toString();
            if (group.length() == 0) {
                throw new IllegalArgumentException("invalid groupkey:" + groupKey);
            }
        } else {
            tenant = sb.toString();
            if (group.length() == 0) {
                throw new IllegalArgumentException("invalid groupkey:" + groupKey);
            }
        }

        return new String[]{dataId, group, tenant};
    }

    /**
     * + -> %2B % -> %25.
     */
    public static void urlEncode(String str, StringBuilder sb) {
        for (int idx = 0; idx < str.length(); ++idx) {
            char c = str.charAt(idx);
            if ('+' == c) {
                sb.append("%2B");
            } else if ('%' == c) {
                sb.append("%25");
            } else {
                sb.append(c);
            }
        }
    }
}