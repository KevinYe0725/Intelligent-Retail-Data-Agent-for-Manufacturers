package com.kevinye.server.controller.userController;

import com.kevinye.pojo.Entity.Problem;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.mapper.ProblemMapper;
import com.kevinye.server.service.ProblemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserProblemController")
@RequestMapping("/user/problem")
public class ProblemController {
    private final ProblemService problemService;
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping("/upload")
    public Result<String> uploadProblem(@RequestBody Problem problem) {
        boolean b = problemService.uploadProblem(problem);
        if(!b) {
            return Result.error("上传内容不得为空");
        }
        return Result.success("上传成功");
    }
}
