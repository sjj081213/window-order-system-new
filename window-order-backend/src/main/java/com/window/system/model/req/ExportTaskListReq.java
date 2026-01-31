package com.window.system.model.req;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExportTaskListReq {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    
    private String operatorName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    // For internal use
    private Long currentUserId;
    private String currentUserRole;
    
    public int getStartIndex() {
        return (pageNo - 1) * pageSize;
    }
}
