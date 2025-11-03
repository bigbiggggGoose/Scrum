package net.lab1024.sa.admin.module.template.dao;

import java.util.List;
import net.lab1024.sa.admin.module.template.domain.entity.TemplateEntity;
import net.lab1024.sa.admin.module.template.domain.form.TemplateQueryForm;
import net.lab1024.sa.admin.module.template.domain.vo.TemplateVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 站内信/邮件模板表 Dao
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@Mapper
public interface TemplateDao extends BaseMapper<TemplateEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<TemplateVO> queryPage(Page page, @Param("queryForm") TemplateQueryForm queryForm);

}
