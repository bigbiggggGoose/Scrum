package net.lab1024.sa.admin.module.scrumsprintbacklog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.entity.ScrumSprintBacklogEntity;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogQueryForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.vo.ScrumSprintBacklogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Sprint待办事项表 Dao
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@Mapper
public interface ScrumSprintBacklogDao extends BaseMapper<ScrumSprintBacklogEntity> {

    /**
     * 分页查询
     * @param page
     * @param queryForm
     * @return
     */
    List<ScrumSprintBacklogVO> queryPage(Page page, @Param("queryForm") ScrumSprintBacklogQueryForm queryForm);

    /**
     * 根据Sprint ID查询待办事项
     * @param sprintId
     * @return
     */
    List<ScrumSprintBacklogVO> queryBySprintId(@Param("sprintId") Long sprintId);

}