package cn.xldeng.server.controller;

import cn.xldeng.common.toolkit.ContentUtil;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.common.web.base.Results;
import cn.xldeng.server.constant.Constants;
import cn.xldeng.server.event.LocalDataChangeEvent;
import cn.xldeng.server.model.ConfigAllInfo;
import cn.xldeng.server.model.ConfigInfoBase;
import cn.xldeng.server.service.ConfigChangePublisher;
import cn.xldeng.server.service.ConfigService;
import cn.xldeng.server.service.ConfigServletInner;
import cn.xldeng.server.toolkit.Md5ConfigUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @program: threadpool
 * @description: 服务端配置控制器
 * @author: dengxinlin
 * @create: 2022-06-27 07:26
 */
@RestController
@RequestMapping(Constants.CONFIG_CONTROLLER_PATH)
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigServletInner configServletInner;

    @GetMapping
    public Result<ConfigInfoBase> detailConfigInfo(
            @RequestParam("tpId") String tpId,
            @RequestParam("itemId") String itemId,
            @RequestParam(value = "namespace", required = false, defaultValue = "") String namespace
    ) {
        return Results.success(configService.findConfigAllInfo(tpId, itemId, namespace));
    }

    @PostMapping
    public Result<Boolean> publishConfig(HttpServletRequest request, @RequestBody ConfigAllInfo config) {
        configService.insertOrUpdate(config);
        ConfigChangePublisher.notifyConfigChange(new LocalDataChangeEvent(ContentUtil.getGroupKey(config)));
        return Results.success(true);
    }

    @SneakyThrows
    @PostMapping("/listener")
    public void listener(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

        String probeModify = request.getParameter(Constants.LISTENING_CONFIGS);
        if (StringUtils.isEmpty(probeModify)) {
            throw new IllegalArgumentException("invalid probeModify");
        }
        probeModify = URLDecoder.decode(probeModify, Constants.ENCODE);
        Map<String,String> clientMd5Map;
        try {
            clientMd5Map = Md5ConfigUtil.getClientMd5Map(probeModify);
        }catch (Exception e){
            throw new IllegalArgumentException("invalid probeModify");
        }
        configServletInner.doPollingConfig(request, response, clientMd5Map, probeModify.length());
        //TODO
    }

}