package cn.xldeng.server.service;

import cn.xldeng.server.model.biz.item.ItemQueryReqDTO;
import cn.xldeng.server.model.biz.item.ItemRespDTO;
import cn.xldeng.server.model.biz.item.ItemSaveReqDTO;
import cn.xldeng.server.model.biz.item.ItemUpdateReqDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:38
 */
public interface ItemService {
    /**
     * 分页查询
     *
     * @param reqDTO
     * @return
     */
    IPage<ItemRespDTO> queryItemPage(ItemQueryReqDTO reqDTO);

    /**
     * 根据 Id 获取项目
     *
     * @param itemId
     * @return
     */
    ItemRespDTO queryItemById(String itemId);

    /**
     * 查询项目
     *
     * @param reqDTO
     * @return
     */
    List<ItemRespDTO> queryItem(ItemQueryReqDTO reqDTO);

    /**
     * 新增项目
     *
     * @param reqDTO
     */
    void saveItem(ItemSaveReqDTO reqDTO);

    /**
     * 更新项目
     *
     * @param reqDTO
     */
    void updateItem(ItemUpdateReqDTO reqDTO);
}