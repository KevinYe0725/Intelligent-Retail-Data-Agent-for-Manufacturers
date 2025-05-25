package com.kevinye.server.service;

import com.kevinye.pojo.Entity.Auditor;
import com.kevinye.pojo.VO.AuditorVO;

import java.util.List;

public interface AuditorService {

    List<AuditorVO> getAllAuditors(String name, String marketName);

    boolean addAuditor(Auditor auditor);

    boolean updateAuditor(Auditor auditor);

    boolean deleteAuditorById(Integer auditorId);
}
