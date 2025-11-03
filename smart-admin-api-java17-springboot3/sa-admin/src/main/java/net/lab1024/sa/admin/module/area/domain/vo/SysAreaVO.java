package net.lab1024.sa.admin.module.area.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 地区表（省/市/区三级联动） 列表VO
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Data
public class SysAreaVO {

    @Schema(description = "区域编码（主键）")
    private String id;

    @Schema(description = "区域名称（如北京市、朝阳区）")
    private String name;

    @Schema(description = "上级区域ID（0=顶级区域）")
    private String pid;

    @Schema(description = "区域简称（如北京、朝阳）")
    private String simplename;

    @Schema(description = "区域等级（1=省/直辖市，2=市，3=区/县）")
    private Integer level;

    @Schema(description = "城市编码（如110000=北京市）")
    private String citycode;

    @Schema(description = "邮政编码")
    private String zipcode;

    @Schema(description = "组合名称（如北京市朝阳区）")
    private String mername;

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "区域拼音（如Beijing）")
    private String pinyin;

}
