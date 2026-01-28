package com.window.system.controller;

import com.window.system.common.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        } else {
            String ct = file.getContentType();
            if (ct != null) {
                if (ct.startsWith("image/jpeg")) ext = ".jpg";
                else if (ct.startsWith("image/png")) ext = ".png";
                else if (ct.startsWith("image/gif")) ext = ".gif";
                else if (ct.startsWith("application/pdf")) ext = ".pdf";
                else if (ct.startsWith("application/vnd.ms-excel")) ext = ".xls";
                else if (ct.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) ext = ".xlsx";
                else if (ct.startsWith("application/msword")) ext = ".doc";
                else if (ct.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) ext = ".docx";
                else ext = "";
            }
        }
        String dateDir = LocalDate.now().toString();
        Path root = Paths.get(System.getProperty("user.dir")).resolve("uploads").resolve(dateDir);
        Files.createDirectories(root);
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = root.resolve(filename);
        Files.write(target, file.getBytes());
        String url = "/api/file/" + dateDir + "/" + filename;
        return Result.success(url);
    }

    @GetMapping("/{date}/{filename}")
    public ResponseEntity<byte[]> get(@PathVariable String date, @PathVariable String filename) throws IOException {
        Path file = Paths.get(System.getProperty("user.dir")).resolve("uploads").resolve(date).resolve(filename);
        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }
        byte[] bytes = Files.readAllBytes(file);
        String contentType = Files.probeContentType(file);
        if (!StringUtils.hasText(contentType)) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".png")) contentType = MediaType.IMAGE_PNG_VALUE;
            else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) contentType = MediaType.IMAGE_JPEG_VALUE;
            else if (lower.endsWith(".gif")) contentType = MediaType.IMAGE_GIF_VALUE;
            else if (lower.endsWith(".pdf")) contentType = MediaType.APPLICATION_PDF_VALUE;
            else contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(bytes);
    }
}
