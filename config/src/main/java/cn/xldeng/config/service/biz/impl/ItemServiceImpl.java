package cn.xldeng.config.service.biz.impl;

import cn.xldeng.config.enums.DelEnum;
import cn.xldeng.config.mapper.ItemInfoMapper;
import cn.xldeng.config.model.ItemInfo;
import cn.xldeng.config.model.biz.item.ItemQueryReqDTO;
import cn.xldeng.config.model.biz.item.ItemRespDTO;
import cn.xldeng.config.model.biz.item.ItemSaveReqDTO;
import cn.xldeng.config.model.biz.item.ItemUpdateReqDTO;
import cn.xldeng.config.model.biz.threadpool.ThreadPoolRespDTO;
import cn.xldeng.config.service.biz.ItemService;
import cn.xldeng.config.service.biz.ThreadPoolService;
import cn.xldeng.config.toolkit.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:40
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ItemInfoMapper itemInfoMapper;
    @Resource
    private ThreadPoolService threadPoolService;

    @Override
    public IPage<ItemRespDTO> queryItemPage(ItemQueryReqDTO reqDTO) {
        LambdaQueryWrapper<ItemInfo> wrapper = Wrappers.lambdaQuery(ItemInfo.class)
                .eq(!StringUtils.isEmpty(reqDTO.getItemId()), ItemInfo::getItemId, reqDTO.getItemId())
                .eq(!StringUtils.isEmpty(reqDTO.getItemName()), ItemInfo::getItemName, reqDTO.getItemName())
                .eq(!StringUtils.isEmpty(reqDTO.getTenantId()), ItemInfo::getTenantId, reqDTO.getTenantId())
                .eq(!StringUtils.isEmpty(reqDTO.getOwner()), ItemInfo::getOwner, reqDTO.getOwner());
        Page<ItemInfo> resultPage = itemInfoMapper.selectPage(reqDTO, wrapper);

        return resultPage.convert(each -> BeanUtil.convert(each, ItemRespDTO.class));
    }

    @Override
    public ItemRespDTO queryItemById(String namespace, String itemId) {
        LambdaQueryWrapper<ItemInfo> queryWrapper = Wrappers
                .lambdaQuery(ItemInfo.class)
                .eq(ItemInfo::getTenantId, namespace)
                .eq(ItemInfo::getItemId, itemId);
        ItemInfo itemInfo = itemInfoMapper.selectOne(queryWrapper);

        ItemRespDTO result = BeanUtil.convert(itemInfo, ItemRespDTO.class);
        return result;
    }

    @Override
    public List<ItemRespDTO> queryItem(ItemQueryReqDTO reqDTO) {
        LambdaQueryWrapper<ItemInfo> wrapper = Wrappers.lambdaQuery(ItemInfo.class)
                .eq(!org.apache.commons.lang3.StringUtils.isEmpty(reqDTO.getItemId()), ItemInfo::getItemId, reqDTO.getItemId())
                .eq(!org.apache.commons.lang3.StringUtils.isEmpty(reqDTO.getTenantId()), ItemInfo::getTenantId, reqDTO.getTenantId());

        List<ItemInfo> itemInfos = itemInfoMapper.selectList(wrapper);
        return BeanUtil.convert(itemInfos, ItemRespDTO.class);
    }

    @Override
    public void saveItem(ItemSaveReqDTO reqDTO) {
        ItemInfo itemInfo = BeanUtil.convert(reqDTO, ItemInfo.class);
        int insertResult = itemInfoMapper.insert(itemInfo);

        boolean retBool = SqlHelper.retBool(insertResult);
        if (!retBool) {
            throw new RuntimeException("插入失败.");
        }
    }

    @Override
    public void updateItem(ItemUpdateReqDTO reqDTO) {
        ItemInfo itemInfo = BeanUtil.convert(reqDTO, ItemInfo.class);
        int updateResult = itemInfoMapper.update(itemInfo, Wrappers
                .lambdaUpdate(ItemInfo.class)
                .eq(ItemInfo::getTenantId, reqDTO.getNamespace())
                .eq(ItemInfo::getItemId, reqDTO.getItemId()));
        boolean retBool = SqlHelper.retBool(updateResult);
        if (!retBool) {
            throw new RuntimeException("修改失败.");
        }
    }

    @Override
    public void deleteItem(String namespace, String itemId) {
        List<ThreadPoolRespDTO> itemList = threadPoolService.getThreadPoolByItemId(itemId);
        if (CollectionUtils.isNotEmpty(itemList)) {
            throw new RuntimeException("项目包含线程池引用, 删除失败.");
        }
        int updateResult = itemInfoMapper.update(new ItemInfo(),
                Wrappers.lambdaUpdate(ItemInfo.class)
                        .eq(ItemInfo::getTenantId, namespace)
                        .eq(ItemInfo::getItemId, itemId)
                        .set(ItemInfo::getDelFlag, DelEnum.DELETE.getIntCode()));
        boolean retBool = SqlHelper.retBool(updateResult);
        if (!retBool) {
            throw new RuntimeException("删除失败.");
        }
    }
}