package net.lab1024.sa.admin.module.template.service;

import java.util.List;
import net.lab1024.sa.admin.module.template.dao.TemplateDao;
import net.lab1024.sa.admin.module.template.domain.entity.TemplateEntity;
import net.lab1024.sa.admin.module.template.domain.form.TemplateAddForm;
import net.lab1024.sa.admin.module.template.domain.form.TemplateQueryForm;
import net.lab1024.sa.admin.module.template.domain.form.TemplateUpdateForm;
import net.lab1024.sa.admin.module.template.domain.vo.TemplateVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 站内信/邮件模板表 Service
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@Service
public class TemplateService {

    @Resource
    private TemplateDao templateDao;

    /**
     * 分页查询
     */
    public PageResult<TemplateVO> queryPage(TemplateQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<TemplateVO> list = templateDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(TemplateAddForm addForm) {
        TemplateEntity templateEntity = SmartBeanUtil.copy(addForm, TemplateEntity.class);
        templateDao.insert(templateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(TemplateUpdateForm updateForm) {
        TemplateEntity templateEntity = SmartBeanUtil.copy(updateForm, TemplateEntity.class);
        templateDao.updateById(templateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        templateDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        templateDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
