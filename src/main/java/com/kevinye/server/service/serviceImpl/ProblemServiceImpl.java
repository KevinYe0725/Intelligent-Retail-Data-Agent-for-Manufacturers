package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.Problem;
import com.kevinye.server.mapper.ProblemMapper;
import com.kevinye.server.service.ProblemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemMapper problemMapper;

    public ProblemServiceImpl(ProblemMapper problemMapper) {
        this.problemMapper = problemMapper;
    }

    @Override
    public boolean uploadProblem(Problem problem) {
        String content = problem.getContent();
        if(content == null) {
            return false;
        }
        problem.setDatetime(LocalDateTime.now());
        problemMapper.uploadProblem(problem);
        return true;
    }
}
