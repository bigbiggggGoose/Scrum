package net.lab1024.sa.admin.module.users.controller;

import net.lab1024.sa.admin.module.users.domain.form.SysUserAddForm;
import net.lab1024.sa.admin.module.users.domain.form.SysUserQueryForm;
import net.lab1024.sa.admin.module.users.domain.form.SysUserUpdateForm;
import net.lab1024.sa.admin.module.users.domain.vo.SysUserVO;
import net.lab1024.sa.admin.module.users.service.SysUserService;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 用户管理 Controller
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@RestController
@Tag(name = "用户管理")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "分页查询 @author oyt")
    @PostMapping("/sysUser/queryPage")
    @SaCheckPermission("sysUser:query")
    public ResponseDTO<PageResult<SysUserVO>> queryPage(@RequestBody @Valid SysUserQueryForm queryForm) {
        return ResponseDTO.ok(sysUserService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author oyt")
    @PostMapping("/sysUser/add")
    @SaCheckPermission("sysUser:add")
    public ResponseDTO<String> add(@RequestBody @Valid SysUserAddForm addForm) {
        return sysUserService.add(addForm);
    }

    @Operation(summary = "更新 @author oyt")
    @PostMapping("/sysUser/update")
    @SaCheckPermission("sysUser:update")
    public ResponseDTO<String> update(@RequestBody @Valid SysUserUpdateForm updateForm) {
        return sysUserService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author oyt")
    @PostMapping("/sysUser/batchDelete")
    @SaCheckPermission("sysUser:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return sysUserService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author oyt")
    @GetMapping("/sysUser/delete/{id}")
    @SaCheckPermission("sysUser:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return sysUserService.delete(id);
    }
}
