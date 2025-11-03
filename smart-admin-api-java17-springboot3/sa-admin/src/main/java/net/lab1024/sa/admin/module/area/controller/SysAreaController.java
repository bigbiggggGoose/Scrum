package net.lab1024.sa.admin.module.area.controller;

import net.lab1024.sa.admin.module.area.domain.form.SysAreaAddForm;
import net.lab1024.sa.admin.module.area.domain.form.SysAreaQueryForm;
import net.lab1024.sa.admin.module.area.domain.form.SysAreaUpdateForm;
import net.lab1024.sa.admin.module.area.domain.vo.SysAreaVO;
import net.lab1024.sa.admin.module.area.service.SysAreaService;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 地区表（省/市/区三级联动） Controller
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@RestController
@Tag(name = "地区表（省/市/区三级联动）")
public class SysAreaController {

    @Resource
    private SysAreaService sysAreaService;

    @Operation(summary = "分页查询 @author oyt")
    @PostMapping("/sysArea/queryPage")
    @SaCheckPermission("sysArea:query")
    public ResponseDTO<PageResult<SysAreaVO>> queryPage(@RequestBody @Valid SysAreaQueryForm queryForm) {
        return ResponseDTO.ok(sysAreaService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author oyt")
    @PostMapping("/sysArea/add")
    @SaCheckPermission("sysArea:add")
    public ResponseDTO<String> add(@RequestBody @Valid SysAreaAddForm addForm) {
        return sysAreaService.add(addForm);
    }

    @Operation(summary = "更新 @author oyt")
    @PostMapping("/sysArea/update")
    @SaCheckPermission("sysArea:update")
    public ResponseDTO<String> update(@RequestBody @Valid SysAreaUpdateForm updateForm) {
        return sysAreaService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author oyt")
    @PostMapping("/sysArea/batchDelete")
    @SaCheckPermission("sysArea:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<String> idList) {
        return sysAreaService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author oyt")
    @GetMapping("/sysArea/delete/{id}")
    @SaCheckPermission("sysArea:delete")
    public ResponseDTO<String> batchDelete(@PathVariable String id) {
        return sysAreaService.delete(id);
    }

    @Operation(summary = "顶级区域列表")
    @GetMapping("/sysArea/listTop")
    @SaCheckPermission("sysArea:query")
    public ResponseDTO<java.util.List<net.lab1024.sa.admin.module.area.domain.entity.SysAreaEntity>> listTop() {
        return sysAreaService.listTop();
    }

    @Operation(summary = "根据父级ID查询子区域")
    @GetMapping("/sysArea/listChildren")
    @SaCheckPermission("sysArea:query")
    public ResponseDTO<java.util.List<net.lab1024.sa.admin.module.area.domain.entity.SysAreaEntity>> listChildren(@RequestParam String pid) {
        return sysAreaService.listChildren(pid);
    }
}
