package net.lab1024.sa.admin.module.scrumproductbacklogs.dao;

import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.entity.ScrumProductBacklogsEntity;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsQueryForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.vo.ScrumProductBacklogsVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 产品待办事项表 Dao
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@Mapper
public interface ScrumProductBacklogsDao extends BaseMapper<ScrumProductBacklogsEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ScrumProductBacklogsVO> queryPage(Page page, @Param("queryForm") ScrumProductBacklogsQueryForm queryForm);

}
