package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.VideoRequest;
import com.bezkoder.springjwt.repository.VideoRepository;
import com.bezkoder.springjwt.security.services.VideoService;
import jakarta.validation.Valid;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/test")
public class TestController {


  Logger logger = LoggerFactory.getLogger(TestController.class);

  @Autowired
  VideoRepository videoRepository;

  @Autowired
  VideoService videoService;

  List<String> files = new ArrayList<String>();
  private final Path rootLocation = Paths.get("C:\\Users\\fofe\\Desktop\\atelieria\\backend\\spring-boot-spring-security-jwt-authentication-master");
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @PostMapping("/testupload")
  @CrossOrigin(origins = "*")
  public String upload(@Valid @RequestBody VideoRequest videoRequest) {
    return "upload Content"+videoRequest.getUsername();
  }

  @PostMapping("/blur")
  @CrossOrigin(origins = "*")
  public ResponseEntity<String> blurVideo(@RequestParam("videoname") String videoName, @RequestParam("model") String model,@RequestParam("username") String username ) {
    String message;
    try {
      this.videoService.blurVideo("original_"+videoName,model);
      message = "Blur SUCCESS!";
      return ResponseEntity.status(HttpStatus.OK).body(message);
    } catch (Exception e) {
      message = "Failed to blur!";
      logger.error("Error calling REST API", e);
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    }
  }

  @PostMapping("/download")
  @CrossOrigin(origins = "*")
  public String download(@Valid @RequestBody VideoRequest videoRequest) {
    return "upload Content"+videoRequest.getUsername();
  }



  @PostMapping("/upload")
  @CrossOrigin(origins = "*")
  public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("videoname") String videoName, @RequestParam("opacity") String opacity,@RequestParam("username") String username ) {
      String message;
      try {
        try {
          Files.copy(file.getInputStream(), this.rootLocation.resolve("original_"+videoName));
        } catch (Exception e) {
          logger.error("Error calling REST API", e);
          throw new RuntimeException("FAIL!");

        }
        files.add(file.getOriginalFilename());

        message = "Successfully uploaded!";
        return ResponseEntity.status(HttpStatus.OK).body(message);
      } catch (Exception e) {
        message = "Failed to upload!";
        logger.error("Error calling REST API", e);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
      }
  }



  @GetMapping("/download/{filename}")
  @CrossOrigin(origins = "*")
  public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
    // Load file as Resource
    Resource fileResource = new FileSystemResource(rootLocation.resolve(filename));

    // Check if the file exists
    if (!fileResource.exists()) {
      throw new RuntimeException("File not found!");
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(fileResource);
  }


  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
