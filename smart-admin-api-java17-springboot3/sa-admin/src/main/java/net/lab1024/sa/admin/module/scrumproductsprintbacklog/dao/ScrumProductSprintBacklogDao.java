package net.lab1024.sa.admin.module.scrumproductsprintbacklog.dao;

import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.entity.ScrumProductSprintBacklogEntity;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogQueryForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.vo.ScrumProductSprintBacklogVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Sprint与产品待办中间表 Dao
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@Mapper
public interface ScrumProductSprintBacklogDao extends BaseMapper<ScrumProductSprintBacklogEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ScrumProductSprintBacklogVO> queryPage(Page page, @Param("queryForm") ScrumProductSprintBacklogQueryForm queryForm);

}
