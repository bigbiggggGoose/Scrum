package net.lab1024.sa.admin.module.scrum.activities.controller;

import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesAddForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesQueryForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.form.SysActivitiesUpdateForm;
import net.lab1024.sa.admin.module.scrum.activities.domain.vo.SysActivitiesVO;
import net.lab1024.sa.admin.module.scrum.activities.service.SysActivitiesService;
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
 * 系统动态表（全局操作记录） Controller
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@RestController
@Tag(name = "系统动态表（全局操作记录）")
public class SysActivitiesController {

    @Resource
    private SysActivitiesService sysActivitiesService;

    @Operation(summary = "分页查询 @author a")
    @PostMapping("/sysActivities/queryPage")
    @SaCheckPermission("sysActivities:query")
    public ResponseDTO<PageResult<SysActivitiesVO>> queryPage(@RequestBody @Valid SysActivitiesQueryForm queryForm) {
        return ResponseDTO.ok(sysActivitiesService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author a")
    @PostMapping("/sysActivities/add")
    @SaCheckPermission("sysActivities:add")
    public ResponseDTO<String> add(@RequestBody @Valid SysActivitiesAddForm addForm) {
        return sysActivitiesService.add(addForm);
    }

    @Operation(summary = "更新 @author a")
    @PostMapping("/sysActivities/update")
    @SaCheckPermission("sysActivities:update")
    public ResponseDTO<String> update(@RequestBody @Valid SysActivitiesUpdateForm updateForm) {
        return sysActivitiesService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author a")
    @PostMapping("/sysActivities/batchDelete")
    @SaCheckPermission("sysActivities:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return sysActivitiesService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author a")
    @GetMapping("/sysActivities/delete/{id}")
    @SaCheckPermission("sysActivities:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return sysActivitiesService.delete(id);
    }
}
