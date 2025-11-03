package net.lab1024.sa.admin.module.systemconfig.controller;

import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigAddForm;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigQueryForm;
import net.lab1024.sa.admin.module.systemconfig.domain.form.SystemConfigUpdateForm;
import net.lab1024.sa.admin.module.systemconfig.domain.vo.SystemConfigVO;
import net.lab1024.sa.admin.module.systemconfig.service.SystemConfigService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 网站与邮箱配置表 Controller
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@RestController
@Tag(name = "网站与邮箱配置表")
public class SystemConfigController {

    @Resource
    private SystemConfigService systemConfigService;

    @Operation(summary = "分页查询 @author thr")
    @PostMapping("/systemConfig/queryPage")
    @SaCheckPermission("systemConfig:query")
    public ResponseDTO<PageResult<SystemConfigVO>> queryPage(@RequestBody @Valid SystemConfigQueryForm queryForm) {
        return ResponseDTO.ok(systemConfigService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author thr")
    @PostMapping("/systemConfig/add")
    @SaCheckPermission("systemConfig:add")
    public ResponseDTO<String> add(@RequestBody @Valid SystemConfigAddForm addForm) {
        return systemConfigService.add(addForm);
    }

    @Operation(summary = "更新 @author thr")
    @PostMapping("/systemConfig/update")
    @SaCheckPermission("systemConfig:update")
    public ResponseDTO<String> update(@RequestBody @Valid SystemConfigUpdateForm updateForm) {
        return systemConfigService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author thr")
    @PostMapping("/systemConfig/batchDelete")
    @SaCheckPermission("systemConfig:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Integer> idList) {
        return systemConfigService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author thr")
    @GetMapping("/systemConfig/delete/{id}")
    @SaCheckPermission("systemConfig:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Integer id) {
        return systemConfigService.delete(id);
    }
}
