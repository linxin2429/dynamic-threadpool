package cn.xldeng.server.controller;

import cn.xldeng.server.constant.Constants;
import cn.xldeng.server.model.ConfigInfoBase;
import cn.xldeng.server.service.ConfigService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

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

    @GetMapping
    public ConfigInfoBase detailConfigInfo(
            @RequestParam("tpId") String tpId,
            @RequestParam("itemId") String itemId,
            @RequestParam(value = "namespace", required = false, defaultValue = "") String namespace
    ) {
        return configService.findConfigAllInfo(tpId, itemId, namespace);
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
        //TODO
    }

}