package net.lab1024.sa.admin.module.scrumproducts.service;

import net.lab1024.sa.admin.module.scrumproducts.dao.ScrumProductsDao;
import net.lab1024.sa.admin.module.scrumproducts.domain.entity.ScrumProductsEntity;
import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsAddForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsQueryForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsUpdateForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.vo.ScrumProductsVO;
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
 * 产品表 Service
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@Service
public class ScrumProductsService {

    @Resource
    private ScrumProductsDao scrumProductsDao;

    /**
     * 分页查询
     */
    public PageResult<ScrumProductsVO> queryPage(ScrumProductsQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ScrumProductsVO> list = scrumProductsDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(ScrumProductsAddForm addForm) {
        ScrumProductsEntity scrumProductsEntity = SmartBeanUtil.copy(addForm, ScrumProductsEntity.class);
        scrumProductsDao.insert(scrumProductsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(ScrumProductsUpdateForm updateForm) {
        ScrumProductsEntity scrumProductsEntity = SmartBeanUtil.copy(updateForm, ScrumProductsEntity.class);
        scrumProductsDao.updateById(scrumProductsEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        scrumProductsDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        scrumProductsDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
