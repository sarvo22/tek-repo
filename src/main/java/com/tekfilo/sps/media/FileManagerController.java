package com.tekfilo.sps.media;

import com.tekfilo.sps.media.entity.FileData;
import com.tekfilo.sps.media.entity.Folders;
import com.tekfilo.sps.media.service.FileService;
import com.tekfilo.sps.util.FileResponseMessage;
import com.tekfilo.sps.util.SPSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/sps/filemanager")
public class FileManagerController {

    @Autowired
    FileService fileService;

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @RequestMapping(value = "/upload/{tenantuid}",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "application/octet-stream",
                    "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<FileResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                          @PathVariable("tenantuid") String tenantuid) {
        FileResponseMessage responseMessage = new FileResponseMessage();
        try {
            fileService.upload2FileManager(file, tenantuid);
            responseMessage.setResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename());
            responseMessage.setStatus(100);
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseMessage.setResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!");
            responseMessage.setStatus(101);
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/upload/{tenantuid}/{subfoldername}",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "application/octet-stream",
                    "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<FileResponseMessage> uploadModuleFile(@RequestParam("file") MultipartFile file,
                                                                @PathVariable("tenantuid") String tenantuid,
                                                                @PathVariable("subfoldername") String subfoldername) {
        FileResponseMessage responseMessage = new FileResponseMessage();
        try {
            fileService.upload2FileManager(file, tenantuid, subfoldername);
            responseMessage.setResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename());
            responseMessage.setStatus(100);
            responseMessage.setNewFileName(StringUtils.cleanPath(file.getOriginalFilename()));
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseMessage.setResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!");
            responseMessage.setStatus(101);
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/uploaduserpicture",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS},
            allowedHeaders = {"Content-Type", "X-Requested-With", "application/octet-stream",
                    "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"},
            exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
    public ResponseEntity<FileResponseMessage> uploadUserPicture(@RequestParam("file") MultipartFile file) {
        FileResponseMessage responseMessage = new FileResponseMessage();
        try {
            final String newFileName = SPSUtil.getRandomFileName();
            fileService.uploadUserPicture(file, newFileName);
            responseMessage.setResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename());
            responseMessage.setStatus(100);
            responseMessage.setNewFileName(newFileName.concat(StringUtils.cleanPath(file.getOriginalFilename())));
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            responseMessage.setResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!");
            responseMessage.setStatus(101);
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        }
    }


    @PostMapping("/createfolder/{tenantuid}/{foldername}")
    public ResponseEntity<FileResponseMessage> createFolder(@PathVariable("tenantuid") String tenantuid,
                                                            @PathVariable("foldername") String foldername) {
        FileResponseMessage responseMessage = new FileResponseMessage();
        try {
            fileService.createUserDirectories(tenantuid, foldername);
            responseMessage.setResponseMessage("Folder Created");
            responseMessage.setStatus(100);
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        } catch (Exception exception) {
            responseMessage.setResponseMessage("Folder Created error");
            responseMessage.setStatus(101);
            return new ResponseEntity<FileResponseMessage>(responseMessage, HttpStatus.OK);
        }
    }

    @GetMapping("/getfolders/{tenantuid}")
    public ResponseEntity<List<Folders>> getFolders(@PathVariable("tenantuid") String tenantuid) {
        return new ResponseEntity<List<Folders>>(fileService.getFolders(tenantuid), HttpStatus.OK);
    }


    @DeleteMapping("/deleteallfiles/{tenantuid}")

    public void delete(@PathVariable("tenantuid") String tenantuid) {
        fileService.deleteAll(tenantuid);
    }

    @GetMapping("/getfiles/{tenantuid}")
    public ResponseEntity<List<FileData>> getListFiles(@PathVariable("tenantuid") String tenantuid) {
        List<FileData> fileInfos = fileService.loadAllFiles(tenantuid)
                .stream()
                .map(mp -> pathToFileData(mp.toAbsolutePath(), tenantuid))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @GetMapping("/getfilesbyfolder/{tenantuid}/{foldername}")
    public ResponseEntity<List<FileData>> getListFilesByFolder(@PathVariable("tenantuid") String tenantuid,
                                                               @PathVariable("foldername") String foldername) {
        List<FileData> fileInfos = fileService.loadAllFiles(tenantuid, foldername)
                .stream()
                .map(mp -> pathToFileData(mp.toAbsolutePath(), tenantuid))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    private FileData pathToFileData(Path path, String tenantuid) {
        FileData fileData = new FileData();
        try {
            String filename = path.getFileName().toString();
            fileData.setFilename(filename);
            fileData.setUrl(MvcUriComponentsBuilder.fromMethodName(FileManagerController.class, "getFile", filename, tenantuid)
                    .build()
                    .toString());
            fileData.setSize(Files.size(path));
            fileData.setSizeText(this.readableFileSize(fileData.getSize()));
            fileData.setType(filename.substring(filename.indexOf(".") + 1, filename.length()));
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            fileData.setCreated(attr.creationTime().toString());
            fileData.setLastModified(attr.lastModifiedTime().toString());
            fileData.setLastAccessed(attr.lastAccessTime().toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return fileData;
    }

    @GetMapping("{filename:.+}/{tenantuid:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable("tenantuid") String tenantuid) {
        Resource file = fileService.load(tenantuid, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    @GetMapping("{downloadfile}/{filename}/{tenantid}/{subfolder}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String filename, @PathVariable("tenantid") String tenantuid,
                                             @PathVariable("subfolder") String subFolder) {
        Resource file = fileService.load(tenantuid.concat("/").concat(subFolder), filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping(value = "/get-file/{filename}/{tenantid}/{subfolder}")
    public ResponseEntity<?> getImage(@PathVariable("filename") String fileName,
                                      @PathVariable("tenantid") String tenantid,
                                      @PathVariable("subfolder") String subFolder) throws IOException {
        try {
            Path imagePath = fileService.getFilePath(fileName, tenantid, subFolder);

            if (imagePath != null) {
                log.debug("Getting image from " + imagePath.toString());

                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(imagePath));

                return ResponseEntity
                        .ok()
                        .contentLength(imagePath.toFile().length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                log.debug("Profile photo not found for user " + fileName);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/get-userpicture/{filename}")
    public ResponseEntity<?> getImage(@PathVariable("filename") String fileName) throws IOException {
        try {
            Path imagePath = fileService.getUserPicture(fileName);

            if (imagePath != null) {
                log.debug("Getting image from " + imagePath.toString());

                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(imagePath));

                return ResponseEntity
                        .ok()
                        .contentLength(imagePath.toFile().length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                log.debug("Profile photo not found for user " + fileName);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
