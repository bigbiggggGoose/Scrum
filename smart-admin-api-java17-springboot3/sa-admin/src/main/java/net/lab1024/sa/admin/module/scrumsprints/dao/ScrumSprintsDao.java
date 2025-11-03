package net.lab1024.sa.admin.module.scrumsprints.dao;

import net.lab1024.sa.admin.module.scrumsprints.domain.entity.ScrumSprintsEntity;
import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsQueryForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.vo.ScrumSprintsVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 项目迭代表 Dao
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Mapper
public interface ScrumSprintsDao extends BaseMapper<ScrumSprintsEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ScrumSprintsVO> queryPage(Page page, @Param("queryForm") ScrumSprintsQueryForm queryForm);

}
