package net.lab1024.sa.admin.module.users.dao;

import java.util.List;
import net.lab1024.sa.admin.module.users.domain.entity.SysUserEntity;
import net.lab1024.sa.admin.module.users.domain.form.SysUserQueryForm;
import net.lab1024.sa.admin.module.users.domain.vo.SysUserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 用户信息表 Dao
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<SysUserVO> queryPage(Page page, @Param("queryForm") SysUserQueryForm queryForm);

}
