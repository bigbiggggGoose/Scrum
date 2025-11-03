package net.lab1024.sa.admin.module.template.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信/邮件模板表 分页查询表单
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class TemplateQueryForm extends PageParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "创建人（sysUser.id，数值型匹配）")
    private Long creator;

    @Schema(description = "修改人（sysUser.id，数值型匹配）")
    private Long updater;

}
