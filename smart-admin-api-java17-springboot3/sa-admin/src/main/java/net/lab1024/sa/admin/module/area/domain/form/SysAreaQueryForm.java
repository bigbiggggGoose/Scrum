package net.lab1024.sa.admin.module.area.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 地区表（省/市/区三级联动） 分页查询表单
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SysAreaQueryForm extends PageParam {

    @Schema(description = "区域名称（如北京市、朝阳区）")
    private String name;

}
