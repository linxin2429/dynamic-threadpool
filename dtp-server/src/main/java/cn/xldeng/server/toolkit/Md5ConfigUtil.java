package cn.xldeng.server.toolkit;

import cn.xldeng.common.toolkit.GroupKey;
import cn.xldeng.common.toolkit.Md5Util;
import cn.xldeng.server.model.ConfigAllInfo;
import cn.xldeng.server.service.ConfigCacheService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static cn.xldeng.common.constant.Constants.LINE_SEPARATOR;
import static cn.xldeng.common.constant.Constants.WORD_SEPARATOR;
/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-28 22:25
 */
public class Md5ConfigUtil {

    static final char WORD_SEPARATOR_CHAR = (char) 2;

    static final char LINE_SEPARATOR_CHAR = (char) 1;

    public static String getTpContentMd5(ConfigAllInfo config) {
        return Md5Util.getTpContentMd5(config);
    }

    public static List<String> compareMd5(HttpServletRequest request, Map<String, String> clientMd5Map) {
        List<String> changedGroupKeys = new ArrayList<>();
        clientMd5Map.forEach((key, val) -> {
            String remoteIp = RequestUtil.getRemoteIp(request);
            boolean isUpdateData = ConfigCacheService.isUpdateData(key, val, remoteIp);
            if (!isUpdateData) {
                changedGroupKeys.add(key);
            }
        });
        return changedGroupKeys;
    }

    public static Map<String, String> getClientMd5Map(String configKeysString) {
        Map<String, String> md5Map = new HashMap(5);

        if (null == configKeysString || "".equals(configKeysString)) {
            return md5Map;
        }
        int start = 0;
        List<String> tmpList = new ArrayList<>(3);
        for (int i = start; i < configKeysString.length(); i++) {
            char c = configKeysString.charAt(i);
            if (c == WORD_SEPARATOR_CHAR) {
                tmpList.add(configKeysString.substring(start, i));
                start = i + 1;
                if (tmpList.size() > 3) {
                    throw new IllegalArgumentException("invalid protocol,too much key");
                }
            } else if (c == LINE_SEPARATOR_CHAR) {
                String endValue = "";
                if (start + 1 <= i) {
                    endValue = configKeysString.substring(start, i);
                }
                start = i + 1;
                if (tmpList.size() == 2) {
                    String groupKey = getKey(tmpList.get(0), tmpList.get(1));
                    groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
                    md5Map.put(groupKey, endValue);
                } else {
                    String groupKey = getKey(tmpList.get(0), tmpList.get(1), endValue);
                    groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
                    md5Map.put(groupKey, tmpList.get(2));
                }
                tmpList.clear();

                // Protect malformed messages
                if (md5Map.size() > 10000) {
                    throw new IllegalArgumentException("invalid protocol, too much listener");
                }
            }
        }
        return md5Map;
    }

    public static String getKey(String dataId, String group) {
        StringBuilder sb = new StringBuilder();
        GroupKey.urlEncode(dataId, sb);
        sb.append('+');
        GroupKey.urlEncode(group, sb);
        return sb.toString();
    }

    public static String getKey(String dataId, String group, String tenant) {
        StringBuilder sb = new StringBuilder();
        GroupKey.urlEncode(dataId, sb);
        sb.append('+');
        GroupKey.urlEncode(group, sb);
        if (!StringUtils.isEmpty(tenant)) {
            sb.append('+');
            GroupKey.urlEncode(tenant, sb);
        }
        return sb.toString();
    }

    public static String compareMd5ResultString(List<String> changedGroupKeys) throws IOException {
        if (null == changedGroupKeys) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String groupKey : changedGroupKeys) {
            String[] dataIdGroupId = GroupKey.parseKey(groupKey);
            sb.append(dataIdGroupId[0]);
            sb.append(WORD_SEPARATOR);
            sb.append(dataIdGroupId[1]);
            // if have tenant, then set it
            if (dataIdGroupId.length == 3) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(dataIdGroupId[2])) {
                    sb.append(WORD_SEPARATOR);
                    sb.append(dataIdGroupId[2]);
                }
            }
            sb.append(LINE_SEPARATOR);
        }

        return URLEncoder.encode(sb.toString(), "UTF-8");
    }
}