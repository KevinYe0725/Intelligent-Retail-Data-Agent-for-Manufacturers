package com.kevinye.server.service;

import com.kevinye.pojo.Entity.Assignment;

import java.time.LocalDate;
import java.util.List;

public interface AssignmentService {
    void addAssignments(List<Integer> storageIds);

    List<Assignment> getAllAssignment(LocalDate date, String marketName);

    void deleteAssignmentById(Integer assignmentId);

    void updateStatus(Integer assignmentId, Integer status);
}
