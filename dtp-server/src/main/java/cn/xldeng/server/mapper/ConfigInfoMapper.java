package cn.xldeng.server.mapper;

import cn.xldeng.server.model.ConfigAllInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: threadpool
 * @description:
 * @author: dengxinlin
 * @create: 2022-06-30 16:22
 */
@Mapper
public interface ConfigInfoMapper extends BaseMapper<ConfigAllInfo> {
}