package net.lab1024.sa.admin.module.scrumsprintbacklog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.scrumsprintbacklog.dao.ScrumSprintBacklogDao;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.entity.ScrumSprintBacklogEntity;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogAddForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogQueryForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogUpdateForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.vo.ScrumSprintBacklogVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Sprint待办事项表 Service
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@Service
public class ScrumSprintBacklogService {

    @Autowired
    private ScrumSprintBacklogDao scrumSprintBacklogDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<ScrumSprintBacklogVO> queryPage(ScrumSprintBacklogQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ScrumSprintBacklogVO> list = scrumSprintBacklogDao.queryPage(page, queryForm);
        PageResult<ScrumSprintBacklogVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
    }

    /**
     * 添加
     *
     * @param addForm
     * @return
     */
    public ResponseDTO<String> add(ScrumSprintBacklogAddForm addForm) {
        ScrumSprintBacklogEntity entity = SmartBeanUtil.copy(addForm, ScrumSprintBacklogEntity.class);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        scrumSprintBacklogDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> update(ScrumSprintBacklogUpdateForm updateForm) {
        ScrumSprintBacklogEntity entity = SmartBeanUtil.copy(updateForm, ScrumSprintBacklogEntity.class);
        entity.setUpdateTime(LocalDateTime.now());
        scrumSprintBacklogDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public ResponseDTO<String> delete(Long id) {
        ScrumSprintBacklogEntity entity = new ScrumSprintBacklogEntity();
        entity.setId(id);
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        scrumSprintBacklogDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 根据Sprint ID查询待办事项
     *
     * @param sprintId
     * @return
     */
    public List<ScrumSprintBacklogVO> queryBySprintId(Long sprintId) {
        return scrumSprintBacklogDao.queryBySprintId(sprintId);
    }

}