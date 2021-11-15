package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Position;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejian
 * @since 2021-09-02
 */
public interface PositionMapper extends BaseMapper<Position> {
    /**
     * 因为name是唯一值，如果用insert插入会出现冲突
     * 所以使用replace into进行插入操作
     * 但是慎用
     * @param name
     * @return
     */
    @Insert("REPLACE INTO t_position(NAME) VALUES (#{pos.getName})")
    int addPosition(String name);

    int selectIdByName(String name);

    Position selectPositionById(Integer id);

}
