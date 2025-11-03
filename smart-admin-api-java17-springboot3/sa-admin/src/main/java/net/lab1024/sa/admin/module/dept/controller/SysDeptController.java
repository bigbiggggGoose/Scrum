package net.lab1024.sa.admin.module.dept.controller;

import net.lab1024.sa.admin.module.dept.domain.form.SysDeptAddForm;
import net.lab1024.sa.admin.module.dept.domain.form.SysDeptQueryForm;
import net.lab1024.sa.admin.module.dept.domain.form.SysDeptUpdateForm;
import net.lab1024.sa.admin.module.dept.domain.vo.SysDeptVO;
import net.lab1024.sa.admin.module.dept.service.SysDeptService;
import net.lab1024.sa.base.common.domain.ValidateList;
import java.util.List;
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
 * 部门管理 Controller
 *
 * @Author oyt
 * @Date 2025-10-11 17:57:33
 * @Copyright oyt
 */

@RestController
@Tag(name = "部门管理")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @Operation(summary = "分页查询 @author oyt")
    @PostMapping("/sysDept/queryPage")
    @SaCheckPermission("sysDept:query")
    public ResponseDTO<PageResult<SysDeptVO>> queryPage(@RequestBody @Valid SysDeptQueryForm queryForm) {
        return ResponseDTO.ok(sysDeptService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author oyt")
    @PostMapping("/sysDept/add")
    @SaCheckPermission("sysDept:add")
    public ResponseDTO<String> add(@RequestBody @Valid SysDeptAddForm addForm) {
        return sysDeptService.add(addForm);
    }

    @Operation(summary = "更新 @author oyt")
    @PostMapping("/sysDept/update")
    @SaCheckPermission("sysDept:update")
    public ResponseDTO<String> update(@RequestBody @Valid SysDeptUpdateForm updateForm) {
        return sysDeptService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author oyt")
    @PostMapping("/sysDept/batchDelete")
    @SaCheckPermission("sysDept:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return sysDeptService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author oyt")
    @GetMapping("/sysDept/delete/{id}")
    @SaCheckPermission("sysDept:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return sysDeptService.delete(id);
    }

    @Operation(summary = "查询全部部门，用于构建树 @author oyt")
    @GetMapping("/sysDept/listAll")
    @SaCheckPermission("sysDept:query")
    public ResponseDTO<List<SysDeptVO>> listAll() {
        return sysDeptService.listAll();
    }
}
