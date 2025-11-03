package net.lab1024.sa.admin.module.scrumproducts.dao;

import net.lab1024.sa.admin.module.scrumproducts.domain.entity.ScrumProductsEntity;
import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsQueryForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.vo.ScrumProductsVO;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 产品表 Dao
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@Mapper
public interface ScrumProductsDao extends BaseMapper<ScrumProductsEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<ScrumProductsVO> queryPage(Page page, @Param("queryForm") ScrumProductsQueryForm queryForm);

}
