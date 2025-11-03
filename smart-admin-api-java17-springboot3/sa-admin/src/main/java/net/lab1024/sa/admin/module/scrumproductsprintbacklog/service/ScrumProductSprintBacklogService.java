package net.lab1024.sa.admin.module.scrumproductsprintbacklog.service;

import net.lab1024.sa.admin.module.scrumproductsprintbacklog.dao.ScrumProductSprintBacklogDao;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.entity.ScrumProductSprintBacklogEntity;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogAddForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogQueryForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogUpdateForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.vo.ScrumProductSprintBacklogVO;
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
 * Sprint与产品待办中间表 Service
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@Service
public class ScrumProductSprintBacklogService {

    @Resource
    private ScrumProductSprintBacklogDao scrumProductSprintBacklogDao;

    /**
     * 分页查询
     */
    public PageResult<ScrumProductSprintBacklogVO> queryPage(ScrumProductSprintBacklogQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ScrumProductSprintBacklogVO> list = scrumProductSprintBacklogDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(ScrumProductSprintBacklogAddForm addForm) {
        ScrumProductSprintBacklogEntity scrumProductSprintBacklogEntity = SmartBeanUtil.copy(addForm, ScrumProductSprintBacklogEntity.class);
        scrumProductSprintBacklogDao.insert(scrumProductSprintBacklogEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(ScrumProductSprintBacklogUpdateForm updateForm) {
        ScrumProductSprintBacklogEntity scrumProductSprintBacklogEntity = SmartBeanUtil.copy(updateForm, ScrumProductSprintBacklogEntity.class);
        scrumProductSprintBacklogDao.updateById(scrumProductSprintBacklogEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        scrumProductSprintBacklogDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        scrumProductSprintBacklogDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
