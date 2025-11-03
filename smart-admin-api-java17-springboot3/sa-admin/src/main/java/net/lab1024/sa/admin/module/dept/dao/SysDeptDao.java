package net.lab1024.sa.admin.module.dept.dao;

import java.util.List;
import net.lab1024.sa.admin.module.dept.domain.entity.SysDeptEntity;
import net.lab1024.sa.admin.module.dept.domain.form.SysDeptQueryForm;
import net.lab1024.sa.admin.module.dept.domain.vo.SysDeptVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 部门管理 Dao
 *
 * @Author oyt
 * @Date 2025-10-11 17:57:33
 * @Copyright oyt
 */

@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SysDeptVO> queryPage(Page page, @Param("queryForm") SysDeptQueryForm queryForm);

    /**
     * 查询全部部门
     */
    List<SysDeptVO> listAll();

}
