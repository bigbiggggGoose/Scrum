package net.lab1024.sa.admin.module.area.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 地区表（省/市/区三级联动） 新建表单
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Data
public class SysAreaAddForm {

    @Schema(description = "区域编码（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域编码不能为空")
    private String id;

    @Schema(description = "区域名称（如北京市、朝阳区）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域名称不能为空")
    private String name;

    @Schema(description = "上级区域ID（0=顶级区域）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "上级区域ID不能为空")
    private String pid;

    @Schema(description = "区域简称（如北京、朝阳）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域简称不能为空")
    private String simplename;

    @Schema(description = "区域等级（1=省/直辖市，2=市，3=区/县）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "区域等级不能为空")
    private Integer level;

    @Schema(description = "城市编码（如110000=北京市）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "城市编码不能为空")
    private String citycode;

    @Schema(description = "邮政编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮政编码不能为空")
    private String zipcode;

    @Schema(description = "组合名称（如北京市朝阳区）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "组合名称不能为空")
    private String mername;

    @Schema(description = "经度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "经度不能为空")
    private java.math.BigDecimal lng;

    @Schema(description = "纬度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "纬度不能为空")
    private java.math.BigDecimal lat;

    @Schema(description = "区域拼音（如Beijing）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域拼音不能为空")
    private String pinyin;

}