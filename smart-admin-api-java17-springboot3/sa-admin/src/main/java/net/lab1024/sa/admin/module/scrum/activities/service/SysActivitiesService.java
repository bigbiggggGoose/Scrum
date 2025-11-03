package net.lab1024.sa.admin.module.scrum.activities.service;

import net.lab1024.sa.admin.module.scrum.activities.dao.SysActivitiesDao;
import net.lab1024.sa.admin.module.scrum.activities.domain.entity.SysActivitiesEntity;
import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesAddForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesQueryForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesUpdateForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.vo.SysActivitiesVO;
import java.util.List;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 系统动态表（全局操作记录） Service
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Service
public class SysActivitiesService {

    @Resource
    private SysActivitiesDao sysActivitiesDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<SysActivitiesVO> queryPage(SysActivitiesQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SysActivitiesVO> list = sysActivitiesDao.queryPage(page, queryForm);
        PageResult<SysActivitiesVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(SysActivitiesAddForm addForm) {
        SysActivitiesEntity sysActivitiesEntity = SmartBeanUtil.copy(addForm, SysActivitiesEntity.class);
        sysActivitiesDao.insert(sysActivitiesEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(SysActivitiesUpdateForm updateForm) {
        SysActivitiesEntity sysActivitiesEntity = SmartBeanUtil.copy(updateForm, SysActivitiesEntity.class);
        sysActivitiesDao.updateById(sysActivitiesEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        sysActivitiesDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        sysActivitiesDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
