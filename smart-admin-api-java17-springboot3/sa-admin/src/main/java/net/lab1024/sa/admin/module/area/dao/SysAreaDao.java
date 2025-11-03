package net.lab1024.sa.admin.module.area.dao;

import java.util.List;
import net.lab1024.sa.admin.module.area.domain.entity.SysAreaEntity;
import net.lab1024.sa.admin.module.area.domain.form.SysAreaQueryForm;
import net.lab1024.sa.admin.module.area.domain.vo.SysAreaVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 地区表（省/市/区三级联动） Dao
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Mapper
public interface SysAreaDao extends BaseMapper<SysAreaEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SysAreaVO> queryPage(Page page, @Param("queryForm") SysAreaQueryForm queryForm);

    /**
     * 查询顶级区域（level=1）
     */
    List<SysAreaEntity> listTop();

    /**
     * 根据父级区域ID查询子区域
     */
    List<SysAreaEntity> listChildren(@Param("pid") String pid);

}
