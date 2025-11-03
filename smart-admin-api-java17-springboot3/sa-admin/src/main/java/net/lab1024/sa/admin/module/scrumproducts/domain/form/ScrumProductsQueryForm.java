package net.lab1024.sa.admin.module.scrumproducts.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 产品表 分页查询表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ScrumProductsQueryForm extends PageParam {

    @Schema(description = "产品ID（可选，用于单产品筛选）")
    private Long productId;
}
