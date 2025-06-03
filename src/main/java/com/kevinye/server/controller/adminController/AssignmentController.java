package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.DTO.StorageIds;
import com.kevinye.pojo.Entity.Assignment;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController("AdminAssignmentController")
@RequestMapping("/admin/assignment")
@Slf4j
public class AssignmentController {
    private final AssignmentService assignmentService;
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/add")
    public Result<String> addAssignments(@RequestBody StorageIds ids){
        List<Integer> storageIds = ids.getStorageIds();
        assignmentService.addAssignments(storageIds);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<List<Assignment>> getAllAssignment(LocalDate date,String marketName){
        List<Assignment> allAssignment = assignmentService.getAllAssignment(date, marketName);
        log.info("任务列表：{}", allAssignment);
        return Result.success(allAssignment);
    }

    @DeleteMapping
    public Result<String> deleteAssignment(Integer assignmentId){
        if (assignmentId == null) {
            throw new RuntimeException("ID不可以为空");
        }
        assignmentService.deleteAssignmentById(assignmentId);
        return Result.success("删除成功");
    }

    @PutMapping("status")
    public Result<String> updateStatus(Integer status,Integer assignmentId){
        if (assignmentId == null||status==null) {
            throw new RuntimeException("参数不可为空");
        }
        assignmentService.updateStatus(assignmentId,status);
        return Result.success("修改成功");
    }
}
