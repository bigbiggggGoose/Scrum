package net.lab1024.sa.admin.module.role.service;

import java.util.List;
import net.lab1024.sa.admin.module.role.dao.SysRoleDao;
import net.lab1024.sa.admin.module.role.domain.entity.SysRoleEntity;
import net.lab1024.sa.admin.module.role.domain.form.SysRoleAddForm;
import net.lab1024.sa.admin.module.role.domain.form.SysRoleQueryForm;
import net.lab1024.sa.admin.module.role.domain.form.SysRoleUpdateForm;
import net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 角色管理 Service
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@Service
public class SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    /**
     * 分页查询
     */
    public PageResult<SysRoleVO> queryPage(SysRoleQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SysRoleVO> list = sysRoleDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(SysRoleAddForm addForm) {
        SysRoleEntity sysRoleEntity = SmartBeanUtil.copy(addForm, SysRoleEntity.class);
        sysRoleDao.insert(sysRoleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(SysRoleUpdateForm updateForm) {
        SysRoleEntity sysRoleEntity = SmartBeanUtil.copy(updateForm, SysRoleEntity.class);
        sysRoleDao.updateById(sysRoleEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        sysRoleDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        sysRoleDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * 查询全部角色（用于前端左侧列表/树）
     */
    public ResponseDTO<java.util.List<net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO>> listAll() {
        java.util.List<net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO> list = sysRoleDao.listAll();
        return net.lab1024.sa.base.common.domain.ResponseDTO.ok(list);
    }

    /**
     * 查询角色详情
     */
    public ResponseDTO<SysRoleVO> get(Long id) {
        if (id == null) {
            return ResponseDTO.ok(null);
        }
        SysRoleEntity entity = sysRoleDao.selectById(id);
        SysRoleVO vo = SmartBeanUtil.copy(entity, SysRoleVO.class);
        return ResponseDTO.ok(vo);
    }
}
