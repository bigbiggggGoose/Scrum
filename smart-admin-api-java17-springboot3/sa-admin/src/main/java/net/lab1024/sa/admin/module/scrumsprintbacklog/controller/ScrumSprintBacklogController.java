package net.lab1024.sa.admin.module.scrumsprintbacklog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogAddForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogQueryForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form.ScrumSprintBacklogUpdateForm;
import net.lab1024.sa.admin.module.scrumsprintbacklog.domain.vo.ScrumSprintBacklogVO;
import net.lab1024.sa.admin.module.scrumsprintbacklog.service.ScrumSprintBacklogService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Sprint待办事项表 Controller
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@RestController
@Tag(name = "Sprint待办事项管理")
public class ScrumSprintBacklogController {

    @Autowired
    private ScrumSprintBacklogService scrumSprintBacklogService;

    @Operation(summary = "分页查询 @author system")
    @PostMapping("/scrumSprintBacklog/queryPage")
    public ResponseDTO<PageResult<ScrumSprintBacklogVO>> queryPage(@RequestBody @Valid ScrumSprintBacklogQueryForm queryForm) {
        return ResponseDTO.ok(scrumSprintBacklogService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author system")
    @PostMapping("/scrumSprintBacklog/add")
    public ResponseDTO<String> add(@RequestBody @Valid ScrumSprintBacklogAddForm addForm) {
        return scrumSprintBacklogService.add(addForm);
    }

    @Operation(summary = "更新 @author system")
    @PostMapping("/scrumSprintBacklog/update")
    public ResponseDTO<String> update(@RequestBody @Valid ScrumSprintBacklogUpdateForm updateForm) {
        return scrumSprintBacklogService.update(updateForm);
    }

    @Operation(summary = "删除 @author system")
    @GetMapping("/scrumSprintBacklog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        return scrumSprintBacklogService.delete(id);
    }

    @Operation(summary = "根据Sprint ID查询待办事项 @author system")
    @GetMapping("/scrumSprintBacklog/queryBySprintId/{sprintId}")
    public ResponseDTO<List<ScrumSprintBacklogVO>> queryBySprintId(@PathVariable Long sprintId) {
        return ResponseDTO.ok(scrumSprintBacklogService.queryBySprintId(sprintId));
    }

}