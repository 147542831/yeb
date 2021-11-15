package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.PoliticsStatus;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface PoliticsStatusMapper extends BaseMapper<PoliticsStatus> {
    int selectIdByName(String name);

    PoliticsStatus selectPoliticsStatusById(Integer id);

}
