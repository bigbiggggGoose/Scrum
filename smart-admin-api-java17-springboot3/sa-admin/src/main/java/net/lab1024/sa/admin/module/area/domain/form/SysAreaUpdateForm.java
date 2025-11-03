package net.lab1024.sa.admin.module.area.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 地区表（省/市/区三级联动） 更新表单
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Data
public class SysAreaUpdateForm {

    @Schema(description = "区域编码（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域编码（主键） 不能为空")
    private String id;

    @Schema(description = "区域名称（如北京市、朝阳区）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域名称（如北京市、朝阳区） 不能为空")
    private String name;

    @Schema(description = "上级区域ID（0=顶级区域）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "上级区域ID（0=顶级区域） 不能为空")
    private String pid;

    @Schema(description = "区域简称（如北京、朝阳）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域简称（如北京、朝阳） 不能为空")
    private String simplename;

    @Schema(description = "区域等级（1=省/直辖市，2=市，3=区/县）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "区域等级（1=省/直辖市，2=市，3=区/县） 不能为空")
    private Integer level;

    @Schema(description = "城市编码（如110000=北京市）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "城市编码（如110000=北京市） 不能为空")
    private String citycode;

    @Schema(description = "邮政编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "邮政编码 不能为空")
    private String zipcode;

    @Schema(description = "组合名称（如北京市朝阳区）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "组合名称（如北京市朝阳区） 不能为空")
    private String mername;

    @Schema(description = "经度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "经度 不能为空")
    private BigDecimal lng;

    @Schema(description = "纬度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "纬度 不能为空")
    private BigDecimal lat;

    @Schema(description = "区域拼音（如Beijing）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "区域拼音（如Beijing） 不能为空")
    private String pinyin;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人（关联sys_user.id，数值型匹配）")
    private Long creator;

    @Schema(description = "修改人（关联sys_user.id，数值型匹配）")
    private Long updater;

}