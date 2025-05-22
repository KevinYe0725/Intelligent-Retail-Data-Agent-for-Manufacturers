package com.kevinye.server.service;

import com.kevinye.pojo.DTO.UploadDTO;

public interface UploadService {
    boolean uploadRemaining(UploadDTO uploadDTO);

    boolean updateRemaining(UploadDTO uploadDTO);
}
