package net.lab1024.sa.admin.module.scrumproductbacklogs.service;

import net.lab1024.sa.admin.module.scrumproductbacklogs.dao.ScrumProductBacklogsDao;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.entity.ScrumProductBacklogsEntity;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsAddForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsQueryForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsUpdateForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.vo.ScrumProductBacklogsVO;
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
 * 产品待办事项表 Service
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@Service
public class ScrumProductBacklogsService {

    @Resource
    private ScrumProductBacklogsDao scrumProductBacklogsDao;

    /**
     * 分页查询
     */
    public PageResult<ScrumProductBacklogsVO> queryPage(ScrumProductBacklogsQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ScrumProductBacklogsVO> list = scrumProductBacklogsDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(ScrumProductBacklogsAddForm addForm) {
        ScrumProductBacklogsEntity scrumProductBacklogsEntity = SmartBeanUtil.copy(addForm, ScrumProductBacklogsEntity.class);
        scrumProductBacklogsDao.insert(scrumProductBacklogsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(ScrumProductBacklogsUpdateForm updateForm) {
        ScrumProductBacklogsEntity scrumProductBacklogsEntity = SmartBeanUtil.copy(updateForm, ScrumProductBacklogsEntity.class);
        scrumProductBacklogsDao.updateById(scrumProductBacklogsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        scrumProductBacklogsDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        scrumProductBacklogsDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
