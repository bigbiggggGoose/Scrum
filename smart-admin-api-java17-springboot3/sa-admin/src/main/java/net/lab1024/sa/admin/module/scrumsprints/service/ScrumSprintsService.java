package net.lab1024.sa.admin.module.scrumsprints.service;

import net.lab1024.sa.admin.module.scrumsprints.dao.ScrumSprintsDao;
import net.lab1024.sa.admin.module.scrumsprints.domain.entity.ScrumSprintsEntity;
import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsAddForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsQueryForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsUpdateForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.vo.ScrumSprintsVO;
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
 * 项目迭代表 Service
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Service
public class ScrumSprintsService {

    @Resource
    private ScrumSprintsDao scrumSprintsDao;

    /**
     * 分页查询
     */
    public PageResult<ScrumSprintsVO> queryPage(ScrumSprintsQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ScrumSprintsVO> list = scrumSprintsDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(ScrumSprintsAddForm addForm) {
        ScrumSprintsEntity scrumSprintsEntity = SmartBeanUtil.copy(addForm, ScrumSprintsEntity.class);
        scrumSprintsDao.insert(scrumSprintsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(ScrumSprintsUpdateForm updateForm) {
        ScrumSprintsEntity scrumSprintsEntity = SmartBeanUtil.copy(updateForm, ScrumSprintsEntity.class);
        scrumSprintsDao.updateById(scrumSprintsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        scrumSprintsDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        scrumSprintsDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
