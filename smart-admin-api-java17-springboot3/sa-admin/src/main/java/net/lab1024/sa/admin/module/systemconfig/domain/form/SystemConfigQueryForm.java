package net.lab1024.sa.admin.module.systemconfig.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站与邮箱配置表 分页查询表单
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemConfigQueryForm extends PageParam {

    @Schema(description = "配置ID（主键）")
    private Integer id;

}
