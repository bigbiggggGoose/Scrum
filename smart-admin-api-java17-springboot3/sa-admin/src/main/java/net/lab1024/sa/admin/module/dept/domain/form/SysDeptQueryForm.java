package net.lab1024.sa.admin.module.dept.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门管理 分页查询表单
 *
 * @Author oyt
 * @Date 2025-10-11 17:57:33
 * @Copyright oyt
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SysDeptQueryForm extends PageParam {

    @Schema(description = "部门名称")
    private String deptName;

}
