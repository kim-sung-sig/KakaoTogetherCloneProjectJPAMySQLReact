package com.example.kakao.domain.view;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.kakao.global.exception.FileStorageException;
import com.example.kakao.global.service.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/", consumes = "multipart/form-data; charset=UTF-8")
    public String index(@RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.storeFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity< ? > getFile(@PathVariable("fileName") String fileName) {
        log.info("fileName => {}", fileName);
        try {
            Resource resource = fileStorageService.loadFileAsResource(fileName);
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                throw new FileStorageException("Could not read the file!");
            }
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.debug("여기문제터짐,, {}", fileName);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/admin")
    public String index2(){
        return "index";
    }
    
}
