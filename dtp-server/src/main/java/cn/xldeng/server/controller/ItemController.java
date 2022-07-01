package cn.xldeng.server.controller;

import cn.xldeng.common.constant.Constants;
import cn.xldeng.common.web.base.Result;
import cn.xldeng.common.web.base.Results;
import cn.xldeng.server.model.biz.item.ItemQueryReqDTO;
import cn.xldeng.server.model.biz.item.ItemRespDTO;
import cn.xldeng.server.model.biz.item.ItemSaveReqDTO;
import cn.xldeng.server.model.biz.item.ItemUpdateReqDTO;
import cn.xldeng.server.service.biz.ItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 15:38
 */
@RestController
@RequestMapping(Constants.BASE_PATH)
public class ItemController {
    @Resource
    private ItemService itemService;

    @PostMapping("/item/query/page")
    public Result<IPage<ItemRespDTO>> queryItemPage(@RequestBody ItemQueryReqDTO reqDTO) {
        return Results.success(itemService.queryItemPage(reqDTO));
    }

    @GetMapping("/item/query/{namespace}/{itemId}")
    public Result queryItemById(@PathParam("namespace") String namespace,@PathParam("itemId") String itemId) {
        return Results.success(itemService.queryItemById(namespace,itemId));
    }

    @PostMapping("/item/save")
    public Result saveItem(@RequestBody ItemSaveReqDTO reqDTO) {
        itemService.saveItem(reqDTO);
        return Results.success();
    }

    @PostMapping("/item/update")
    public Result updateItem(ItemUpdateReqDTO reqDTO) {
        itemService.updateItem(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/item/delete/{namespace}/{itemId}")
    public Result deleteItem(@PathVariable("namespace") String namespace, @PathVariable("itemId") String itemId) {
        itemService.deleteItem(namespace, itemId);
        return Results.success();
    }
}