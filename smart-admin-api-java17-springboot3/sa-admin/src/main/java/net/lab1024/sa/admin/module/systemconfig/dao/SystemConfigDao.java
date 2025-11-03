package net.lab1024.sa.admin.module.systemconfig.dao;

import java.util.List;
import net.lab1024.sa.admin.module.systemconfig.domain.entity.SystemConfigEntity;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigQueryForm;
import net.lab1024.sa.admin.module.systemconfig.domain.vo.SystemConfigVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 网站与邮箱配置表 Dao
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@Mapper
public interface SystemConfigDao extends BaseMapper<SystemConfigEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SystemConfigVO> queryPage(Page page, @Param("queryForm") SystemConfigQueryForm queryForm);

}
