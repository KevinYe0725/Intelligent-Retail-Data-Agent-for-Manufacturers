package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.DTO.UploadDTO;
import com.kevinye.server.mapper.UploadMapper;
import com.kevinye.server.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {
    private final UploadMapper uploadMapper;
    public UploadServiceImpl(UploadMapper uploadMapper) {
        this.uploadMapper = uploadMapper;
    }

    @Override
    public boolean uploadRemaining(UploadDTO uploadDTO) {
        Integer period = uploadDTO.getPeriod();
        if(period == -1){
            return false;
        }
        uploadMapper.uploadRemainingByPeriod(uploadDTO);
        uploadDTO.setStorageId(uploadMapper.selectStorageIdByIds(uploadDTO));
        Integer APSId = uploadMapper.getAuditor(uploadDTO.getPeriod(), uploadDTO.getAuditorId(), uploadDTO.getStorageId());
        if(APSId == null){
            uploadMapper.uploadAuditor(uploadDTO);
        }

        return true;
    }

    @Override
    public boolean updateRemaining(UploadDTO uploadDTO) {
        Integer period = uploadDTO.getPeriod();
        if(period == -1){
            return false;
        }
        uploadDTO.setStorageId(uploadMapper.selectStorageIdByIds(uploadDTO));

        Integer auditorId = uploadMapper.selectAuditorId(uploadDTO);
        if(auditorId == null||!auditorId .equals(uploadDTO.getAuditorId())){
            return false;
        }
        //这个上传方法本质就是修改性质的
        uploadMapper.uploadRemainingByPeriod(uploadDTO);
        return true;
    }
}
