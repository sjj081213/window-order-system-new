package com.window.system.service.export;

import com.alibaba.excel.EasyExcel;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.entity.SysExportTask;
import com.window.system.model.entity.WindowOrder;
import com.window.system.model.req.OrderListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class OrderExportStrategy implements ExportStrategy {

    @Autowired
    private WindowOrderMapper windowOrderMapper;
    
    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Override
    public String getType() {
        return "ORDER";
    }

    @Override
    public File export(String params, Long taskId) throws Exception {
        OrderListReq req = cn.hutool.json.JSONUtil.toBean(params, OrderListReq.class);
        
        // Determine filename
        SysExportTask task = sysExportTaskMapper.getById(taskId);
        String fileName = task.getTaskName();
        if (!fileName.endsWith(".xlsx")) {
            fileName += ".xlsx";
        }
        
        File temp = File.createTempFile("export_", ".xlsx");
        List<WindowOrder> list = windowOrderMapper.exportList(req);
        EasyExcel.write(temp, WindowOrder.class).sheet("订单").doWrite(list);
        
        // Rename
        File finalFile = new File(temp.getParent(), fileName);
        if (temp.renameTo(finalFile)) {
            return finalFile;
        }
        return temp;
    }
}
