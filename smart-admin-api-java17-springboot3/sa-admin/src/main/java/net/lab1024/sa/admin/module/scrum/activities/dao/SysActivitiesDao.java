package net.lab1024.sa.admin.module.scrum.activities.dao;

import net.lab1024.sa.admin.module.scrum.activities.domain.entity.SysActivitiesEntity;
import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesQueryForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.vo.SysActivitiesVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 系统动态表（全局操作记录） Dao
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Mapper
@Component
public interface SysActivitiesDao extends BaseMapper<SysActivitiesEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SysActivitiesVO> queryPage(Page page, @Param("queryForm") SysActivitiesQueryForm queryForm);


}
