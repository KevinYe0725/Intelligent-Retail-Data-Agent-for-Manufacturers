package com.kevinye.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO {
    private Integer goodId;
    private Integer marketId;
    private Integer remaining;
    private Integer auditorId;
    private Integer storageId;
    private LocalDate uploadDate;
    private Integer period;
    private LocalDate date;
}
