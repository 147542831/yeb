package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Joblevel;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface JoblevelMapper extends BaseMapper<Joblevel> {
    int selectIdByName(String name);

    Joblevel selectJoblevelById(Integer id);

}
