package net.lab1024.sa.admin.module.users.service;

import java.util.List;
import net.lab1024.sa.admin.module.users.dao.SysUserDao;
import net.lab1024.sa.admin.module.users.domain.entity.SysUserEntity;
import net.lab1024.sa.admin.module.users.domain.form.SysUserAddForm;
import net.lab1024.sa.admin.module.users.domain.form.SysUserQueryForm;
import net.lab1024.sa.admin.module.users.domain.form.SysUserUpdateForm;
import net.lab1024.sa.admin.module.users.domain.vo.SysUserVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 用户信息表 Service
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@Service
public class SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    /**
     * 分页查询
     */
    public PageResult<SysUserVO> queryPage(SysUserQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SysUserVO> list = sysUserDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(SysUserAddForm addForm) {
        SysUserEntity sysUserEntity = SmartBeanUtil.copy(addForm, SysUserEntity.class);
        sysUserDao.insert(sysUserEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(SysUserUpdateForm updateForm) {
        SysUserEntity sysUserEntity = SmartBeanUtil.copy(updateForm, SysUserEntity.class);
        sysUserDao.updateById(sysUserEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        sysUserDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        sysUserDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
