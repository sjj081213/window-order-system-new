package com.window.system.service;

import com.window.system.common.Result;
import com.window.system.config.MinioConfig;
import com.window.system.mapper.SysExportTaskMapper;
import com.window.system.mapper.WindowOrderMapper;
import com.window.system.model.entity.SysExportTask;
import com.window.system.security.AuthUser;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@Slf4j
public class SysExportTaskService {

    @Autowired
    private SysExportTaskMapper sysExportTaskMapper;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private WindowOrderMapper windowOrderMapper;

    // Self-injection to allow calling @Async methods from within the same class
    @Autowired
    @Lazy
    private SysExportTaskService self;

    public Result<List<SysExportTask>> list() {
        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        // If admin, can see all
        if ("ADMIN".equalsIgnoreCase(user.getRole()) || "admin".equalsIgnoreCase(user.getUsername())) {
            userId = null;
        }
        return Result.success(sysExportTaskMapper.list(userId));
    }


    public Long createTask(String taskName, String exportType, String exportParams) {
        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysExportTask task = new SysExportTask();
        task.setTaskName(taskName);
        task.setExportType(exportType);
        task.setExportParams(exportParams);
        task.setStatus("PENDING");
        task.setCreateBy(user.getId());
        sysExportTaskMapper.insert(task);

        // Trigger async execution immediately using self-proxy
        if (exportType != null) {
            self.executeTask(task.getId(), exportType, exportParams);
        }

        return task.getId();
    }

    // Deprecated or for backward compatibility
    public Long createTask(String taskName) {
        return createTask(taskName, null, null);
    }

    @Async
    public void executeTask(Long taskId, String exportType, String exportParams) {
        if ("ORDER".equals(exportType)) {
            // Handle order export
             this.executeExport(taskId, () -> {
                try {
                    com.window.system.model.req.OrderListReq req = cn.hutool.json.JSONUtil.toBean(exportParams, com.window.system.model.req.OrderListReq.class);
                    // Use task name as filename if it ends with .xlsx, otherwise append it
                    SysExportTask task = sysExportTaskMapper.getById(taskId);
                    String fileName = task.getTaskName();
                    if (!fileName.endsWith(".xlsx")) {
                        fileName += ".xlsx";
                    }
                    
                    File temp = File.createTempFile("export_", ".xlsx");
                    List<com.window.system.model.entity.WindowOrder> list = windowOrderMapper.exportList(req);
                    com.alibaba.excel.EasyExcel.write(temp, com.window.system.model.entity.WindowOrder.class).sheet("订单").doWrite(list);
                    
                    // Rename temp file to match task name for upload
                    File finalFile = new File(temp.getParent(), fileName);
                    if (temp.renameTo(finalFile)) {
                        return finalFile;
                    }
                    return temp;
                } catch (Exception e) {
                    throw new RuntimeException("Order export failed", e);
                }
            });
        } else if ("TEST".equals(exportType)) {
             this.executeExport(taskId, () -> {
                try {
                    // 模拟耗时操作
                    Thread.sleep(3000);
                    File temp = File.createTempFile("test_export_", ".csv");
                    try (java.io.FileWriter writer = new java.io.FileWriter(temp)) {
                        writer.write("ID,Name,Time\n");
                        for (int i = 0; i < 100; i++) {
                            writer.write(i + ",User " + i + "," + System.currentTimeMillis() + "\n");
                        }
                    }
                    return temp;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Async
    public void executeExport(Long taskId, Supplier<File> exportLogic) {
        SysExportTask task = new SysExportTask();
        task.setId(taskId);
        task.setStatus("PROCESSING");
        sysExportTaskMapper.update(task);

        try {
            File file = exportLogic.get();
            if (file == null || !file.exists()) {
                throw new RuntimeException("Export file not found or generation failed");
            }

            // Upload to MinIO
            String dateDir = LocalDate.now().toString();
            String originalFilename = file.getName();
            // Use original filename instead of UUID to keep meaningful names in MinIO
            String objectName = dateDir + "/" + originalFilename;

            try (FileInputStream fis = new FileInputStream(file)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .object(objectName)
                                .stream(fis, file.length(), -1)
                                .contentType("application/vnd.ms-excel")
                                .build());
            }

            String url = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;

            task.setStatus("COMPLETED");
            task.setFileUrl(url);
            task.setFileName(originalFilename);
            task.setFinishTime(LocalDateTime.now());
            sysExportTaskMapper.update(task);

            // Cleanup local file
            try {
                file.delete();
            } catch (Exception ignored) {
            }

        } catch (Exception e) {
            log.error("Export failed for task " + taskId, e);
            task.setStatus("FAILED");
            task.setErrorMsg(e.getMessage());
            task.setFinishTime(LocalDateTime.now());
            sysExportTaskMapper.update(task);
        }
    }
}
