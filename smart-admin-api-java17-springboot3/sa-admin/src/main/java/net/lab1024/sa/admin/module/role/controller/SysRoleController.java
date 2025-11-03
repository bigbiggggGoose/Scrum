package net.lab1024.sa.admin.module.role.controller;

import net.lab1024.sa.admin.module.role.domain.form.SysRoleAddForm;
import net.lab1024.sa.admin.module.role.domain.form.SysRoleQueryForm;
import net.lab1024.sa.admin.module.role.domain.form.SysRoleUpdateForm;
import net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO;
import net.lab1024.sa.admin.module.role.service.SysRoleService;
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
 * 角色管理 Controller
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@RestController
@Tag(name = "角色管理")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Operation(summary = "分页查询 @author oyt")
    @PostMapping("/sysRole/queryPage")
    @SaCheckPermission("sysRole:query")
    public ResponseDTO<PageResult<SysRoleVO>> queryPage(@RequestBody @Valid SysRoleQueryForm queryForm) {
        return ResponseDTO.ok(sysRoleService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author oyt")
    @PostMapping("/sysRole/add")
    @SaCheckPermission("sysRole:add")
    public ResponseDTO<String> add(@RequestBody @Valid SysRoleAddForm addForm) {
        return sysRoleService.add(addForm);
    }

    @Operation(summary = "更新 @author oyt")
    @PostMapping("/sysRole/update")
    @SaCheckPermission("sysRole:update")
    public ResponseDTO<String> update(@RequestBody @Valid SysRoleUpdateForm updateForm) {
        return sysRoleService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author oyt")
    @PostMapping("/sysRole/batchDelete")
    @SaCheckPermission("sysRole:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return sysRoleService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author oyt")
    @GetMapping("/sysRole/delete/{id}")
    @SaCheckPermission("sysRole:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return sysRoleService.delete(id);
    }

    @Operation(summary = "查询全部角色（用于构建列表/树） @author oyt")
    @GetMapping("/sysRole/listAll")
    @SaCheckPermission("sysRole:query")
    public ResponseDTO<java.util.List<net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO>> listAll() {
        return sysRoleService.listAll();
    }

    @Operation(summary = "查询角色详情 @author oyt")
    @GetMapping("/sysRole/get/{id}")
    @SaCheckPermission("sysRole:query")
    public ResponseDTO<net.lab1024.sa.admin.module.role.domain.vo.SysRoleVO> get(@PathVariable Long id) {
        return sysRoleService.get(id);
    }
}
