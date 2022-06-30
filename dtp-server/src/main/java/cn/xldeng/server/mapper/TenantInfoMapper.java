package cn.xldeng.server.mapper;

import cn.xldeng.server.model.TenantInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:23
 */
@Mapper
public interface TenantInfoMapper extends BaseMapper<TenantInfo> {
}