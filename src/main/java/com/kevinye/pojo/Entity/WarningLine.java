package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningLine {
    private Double noonWarningLine;
    private Double afternoonWarningLine;
    private Double nightWaringLine;
}
