package net.lab1024.sa.admin.module.dept.service;

import java.util.List;
import net.lab1024.sa.admin.module.dept.dao.SysDeptDao;
import net.lab1024.sa.admin.module.dept.domain.entity.SysDeptEntity;
import net.lab1024.sa.admin.module.dept.domain.form.SysDeptAddForm;
import net.lab1024.sa.admin.module.dept.domain.form.SysDeptQueryForm;
import net.lab1024.sa.admin.module.dept.domain.form.SysDeptUpdateForm;
import net.lab1024.sa.admin.module.dept.domain.vo.SysDeptVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 部门管理 Service
 *
 * @Author oyt
 * @Date 2025-10-11 17:57:33
 * @Copyright oyt
 */

@Service
public class SysDeptService {

    @Resource
    private SysDeptDao sysDeptDao;

    /**
     * 分页查询
     */
    public PageResult<SysDeptVO> queryPage(SysDeptQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SysDeptVO> list = sysDeptDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(SysDeptAddForm addForm) {
        SysDeptEntity sysDeptEntity = SmartBeanUtil.copy(addForm, SysDeptEntity.class);
        sysDeptDao.insert(sysDeptEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(SysDeptUpdateForm updateForm) {
        SysDeptEntity sysDeptEntity = SmartBeanUtil.copy(updateForm, SysDeptEntity.class);
        sysDeptDao.updateById(sysDeptEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        sysDeptDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        sysDeptDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * 查询全部部门（用于前端构建树）
     */
    public ResponseDTO<List<SysDeptVO>> listAll() {
        List<SysDeptVO> list = sysDeptDao.listAll();
        return ResponseDTO.ok(list);
    }
}
