package net.lab1024.sa.admin.module.role.dao;

import java.util.List;
import net.lab1024.sa.admin.module.role.domain.entity.SysRoleEntity;
import net.lab1024.sa.admin.module.role.domain.form.SysRoleQueryForm;
import net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 角色管理 Dao
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SysRoleVO> queryPage(Page page, @Param("queryForm") SysRoleQueryForm queryForm);

    /**
     * 查询全部角色
     */
    List<SysRoleVO> listAll();

}
