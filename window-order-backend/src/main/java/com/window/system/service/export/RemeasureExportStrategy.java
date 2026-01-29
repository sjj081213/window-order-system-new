package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.RemeasureTaskMapper;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.model.entity.RemeasureTask;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.req.RemeasureTaskListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class RemeasureExportStrategy implements ExportStrategy {

    @Autowired
    private RemeasureTaskMapper remeasureTaskMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    public String getType() {
        return "REMEASURE";
    }

    @Override
    public File export(String params, Long taskId) throws Exception {
        RemeasureTaskListReq req = cn.hutool.json.JSONUtil.toBean(params, RemeasureTaskListReq.class);
        
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_remeasure_", ".xlsx");
        List<RemeasureTask> list = remeasureTaskMapper.exportList(req);
        EasyExcel.write(temp, RemeasureTask.class).sheet("复尺任务").doWrite(list);
        
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
