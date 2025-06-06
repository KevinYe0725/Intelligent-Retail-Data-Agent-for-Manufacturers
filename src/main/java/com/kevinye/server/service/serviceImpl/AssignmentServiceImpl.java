package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.Assignment;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import com.kevinye.server.mapper.AssignmentMapper;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.service.AssignmentService;
import com.kevinye.server.service.MarketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final MarketMapper marketMapper;
    private final AssignmentMapper assignmentMapper;
    public AssignmentServiceImpl(MarketMapper marketMapper, AssignmentMapper assignmentMapper) {
        this.marketMapper = marketMapper;
        this.assignmentMapper = assignmentMapper;
    }
    @Override
    public void addAssignments(List<Integer> storageIds) {
        List<Storage> storages = marketMapper.getStorageByIds(storageIds);
        List<Assignment> assignmentList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        for (Storage storage : storages) {
            String content =storage.getMarketName()+": "+storage.getGoodName()+"品类在"+storage.getDate().format(formatter)+"警告须要处理";
            Assignment assignment = new Assignment(content,storage.getMarketId(),storage.getGoodId(),storage.getDate());
            assignmentList.add(assignment);
        }
        assignmentMapper.addAssignmentList(assignmentList);
        assignmentMapper.updateStatus4Storages(assignmentList);
    }

    @Override
    public List<Assignment> getAllAssignment(LocalDate date, String marketName) {
        List<Assignment> assignmentList = new ArrayList<>();
        if (marketName != null) {
            List<Market> market = marketMapper.getMarketByName(marketName);
            List<Integer> ids = new ArrayList<>();
            market.forEach(m -> ids.add(m.getId()));
             assignmentList = assignmentMapper.getAllAssignmentByIds(date, ids);
        }else {
            assignmentList=assignmentMapper.getAllAssignments(date);
        }
        return assignmentList;
    }

    @Transactional
    @Override
    public void deleteAssignmentById(Integer assignmentId) {
        assignmentMapper.deleteAssignmentById(assignmentId);
        Assignment assignmentById = assignmentMapper.getAssignmentById(assignmentId);
        assignmentMapper.updateStatus4Storage(assignmentById);
    }

    @Override
    public void updateStatus(Integer assignmentId, Integer status) {
        if (status == 1) {
            assignmentMapper.updateStatus(assignmentId,0);
        }else{
            assignmentMapper.updateStatus(assignmentId,1);
        }
    }
}
