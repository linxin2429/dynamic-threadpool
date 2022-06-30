package cn.xldeng.server.service.biz.impl;

import cn.xldeng.server.mapper.ItemInfoMapper;
import cn.xldeng.server.model.ItemInfo;
import cn.xldeng.server.model.biz.item.ItemQueryReqDTO;
import cn.xldeng.server.model.biz.item.ItemRespDTO;
import cn.xldeng.server.model.biz.item.ItemSaveReqDTO;
import cn.xldeng.server.model.biz.item.ItemUpdateReqDTO;
import cn.xldeng.server.service.biz.ItemService;
import cn.xldeng.server.toolkit.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @Override
    public IPage<ItemRespDTO> queryItemPage(ItemQueryReqDTO reqDTO) {
        LambdaQueryWrapper<ItemInfo> wrapper = Wrappers.lambdaQuery(ItemInfo.class)
                .eq(!StringUtils.isEmpty(reqDTO.getItemId()), ItemInfo::getItemId, reqDTO.getItemId())
                .eq(!StringUtils.isEmpty(reqDTO.getItemName()), ItemInfo::getItemName, reqDTO.getItemName())
                .eq(!StringUtils.isEmpty(reqDTO.getTenantId()), ItemInfo::getTenantId, reqDTO.getTenantId())
                .eq(!StringUtils.isEmpty(reqDTO.getOwner()), ItemInfo::getOwner, reqDTO.getOwner());
        Page resultPage = itemInfoMapper.selectPage(reqDTO, wrapper);

        return resultPage.convert(each -> BeanUtil.convert(each, ItemRespDTO.class));
    }

    @Override
    public ItemRespDTO queryItemById(String itemId) {
        LambdaQueryWrapper<ItemInfo> queryWrapper = Wrappers
                .lambdaQuery(ItemInfo.class).eq(ItemInfo::getItemId, itemId);
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
                .lambdaUpdate(ItemInfo.class).eq(ItemInfo::getItemId, reqDTO.getItemId()));
        boolean retBool = SqlHelper.retBool(updateResult);
        if (!retBool) {
            throw new RuntimeException("修改失败.");
        }
    }
}