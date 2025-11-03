package net.lab1024.sa.admin.module.scrumsprints.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目迭代表 分页查询表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ScrumSprintsQueryForm extends PageParam {

    @Schema(description = "产品ID筛选")
    private Long productId;

    @Schema(description = "产品名称关键字")
    private String productName;
}
