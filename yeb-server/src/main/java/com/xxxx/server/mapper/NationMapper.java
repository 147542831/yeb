package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Nation;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface NationMapper extends BaseMapper<Nation> {
    int selectIdByName(String name);

    Nation selectNationById(Integer id);
}
