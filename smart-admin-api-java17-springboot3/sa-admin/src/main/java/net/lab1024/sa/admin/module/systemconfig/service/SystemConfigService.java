package net.lab1024.sa.admin.module.systemconfig.service;

import java.util.List;
import net.lab1024.sa.admin.module.systemconfig.dao.SystemConfigDao;
import net.lab1024.sa.admin.module.systemconfig.domain.entity.SystemConfigEntity;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigAddForm;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigQueryForm;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigUpdateForm;
import net.lab1024.sa.admin.module.systemconfig.domain.vo.SystemConfigVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 网站与邮箱配置表 Service
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@Service
public class SystemConfigService {

    @Resource
    private SystemConfigDao systemConfigDao;

    /**
     * 分页查询
     */
    public PageResult<SystemConfigVO> queryPage(SystemConfigQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SystemConfigVO> list = systemConfigDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(SystemConfigAddForm addForm) {
        SystemConfigEntity systemConfigEntity = SmartBeanUtil.copy(addForm, SystemConfigEntity.class);
        systemConfigDao.insert(systemConfigEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(SystemConfigUpdateForm updateForm) {
        SystemConfigEntity systemConfigEntity = SmartBeanUtil.copy(updateForm, SystemConfigEntity.class);
        systemConfigDao.updateById(systemConfigEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        systemConfigDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Integer id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        systemConfigDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
