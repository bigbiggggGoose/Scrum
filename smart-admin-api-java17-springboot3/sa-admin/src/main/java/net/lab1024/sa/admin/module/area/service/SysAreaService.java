package net.lab1024.sa.admin.module.area.service;

import java.util.List;
import net.lab1024.sa.admin.module.area.dao.SysAreaDao;
import net.lab1024.sa.admin.module.area.domain.entity.SysAreaEntity;
import net.lab1024.sa.admin.module.area.domain.form.SysAreaAddForm;
import net.lab1024.sa.admin.module.area.domain.form.SysAreaQueryForm;
import net.lab1024.sa.admin.module.area.domain.form.SysAreaUpdateForm;
import net.lab1024.sa.admin.module.area.domain.vo.SysAreaVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 地区表（省/市/区三级联动） Service
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Service
public class SysAreaService {

    @Resource
    private SysAreaDao sysAreaDao;

    /**
     * 分页查询
     */
    public PageResult<SysAreaVO> queryPage(SysAreaQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SysAreaVO> list = sysAreaDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(SysAreaAddForm addForm) {
        SysAreaEntity sysAreaEntity = SmartBeanUtil.copy(addForm, SysAreaEntity.class);
        sysAreaDao.insert(sysAreaEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(SysAreaUpdateForm updateForm) {
        SysAreaEntity sysAreaEntity = SmartBeanUtil.copy(updateForm, SysAreaEntity.class);
        sysAreaDao.updateById(sysAreaEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<String> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        sysAreaDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(String id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        sysAreaDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * 顶级区域列表
     */
    public ResponseDTO<List<SysAreaEntity>> listTop() {
        List<SysAreaEntity> list = sysAreaDao.listTop();
        return ResponseDTO.ok(list);
    }

    /**
     * 子区域列表
     */
    public ResponseDTO<List<SysAreaEntity>> listChildren(String pid) {
        List<SysAreaEntity> list = sysAreaDao.listChildren(pid);
        return ResponseDTO.ok(list);
    }
}
