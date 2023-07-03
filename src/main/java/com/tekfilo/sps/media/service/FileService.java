package com.tekfilo.sps.media.service;


import com.tekfilo.sps.media.entity.Folders;
import com.tekfilo.sps.util.OSValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    @Value("${upload.path}")
    String uploadPath;


    public void createUserDirectories(final String userFolderName) {
        try {
            Files.createDirectories(Paths.get(getUserFolderName(userFolderName)));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public void createUserDirectories(final String tenantId, final String userFolderName) {
        try {
            Files.createDirectories(Paths.get(getUserFolderName(tenantId.concat("/").concat(userFolderName))));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Could not able to create folder");
        }

    }

    public String getUserFolderName(final String userFolderName) {
        if (OSValidator.isWindows()) {
            return "C://".concat(uploadPath).concat("//").concat(userFolderName.replaceAll("/", "//"));
        }
        return uploadPath.concat("/").concat(userFolderName);
    }


    public void upload2FileManager(MultipartFile file, final String userFolderName) {
        try {
            Path root = Paths.get(getUserFolderName(userFolderName));
            if (!Files.exists(root)) {
                createUserDirectories(userFolderName);
            }
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void upload2FileManager(MultipartFile file, final String userFolderName, final String subFolderName) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path uploadPath = Paths.get(getUploadDirectory(userFolderName.concat("/").concat(subFolderName)));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        } catch (IOException e) {
            throw new IOException("Could not save image file: ");
        }
    }

    public void uploadUserPicture(MultipartFile file, final String newFileName) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path uploadPath = Paths.get(getUploadDirectory("user"));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(newFileName.concat(fileName));
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + fileName, ioe);
            }
        } catch (IOException e) {
            throw new IOException("Could not save image file: ");
        }
    }

    public String getUploadDirectory(String folder) {
        String directoryPath = uploadPath.endsWith("/") ? uploadPath : uploadPath.concat("/");
        if (OSValidator.isWindows()) {
            final String baseDirectory = "C://" + directoryPath.replaceAll("/", "//") + folder.replaceAll("/", "//");
            return baseDirectory;
        }
        return directoryPath.concat(folder);
    }


    public Resource load(final String userFolderName, String filename) {
        try {
            Path file = Paths.get(getUserFolderName(userFolderName)).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


    public void deleteAll(final String userFolderName) {
        FileSystemUtils.deleteRecursively(Paths.get(getUserFolderName(userFolderName))
                .toFile());
    }


    public List<Path> loadAllFiles(final String userFolderName) {
        try {
            Path root = Paths.get(getUserFolderName(userFolderName));
            if (Files.exists(root)) {
                return Files.walk(root, 1)
                        .filter(path -> !path.equals(root))
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }


    public List<Path> loadAllFiles(final String tenantUid, final String folderName) {
        try {
            Path root = Paths.get(getUserFolderName(tenantUid.concat("/").concat(folderName)));
            if (Files.exists(root)) {
                return Files.walk(root, 1)
                        .filter(path -> !path.equals(root))
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }

    public List<Folders> getFolders(String tenantuid) {
        List<Folders> foldersList = new ArrayList<>();
        try {
            File dir = new File(getUserFolderName(tenantuid));
            File[] files = dir.listFiles();
            if (files != null) {
                if (files.length > 0) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            Folders folders = new Folders();
                            folders.setFolderName(file.getName());
                            foldersList.add(folders);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return foldersList;
    }

    public Path getFilePath(String fileName, String userFolderName, String subFolderName) throws Exception {
        Path imagePath = Paths.get(this.getUploadDirectory(userFolderName.concat("/").concat(subFolderName).concat("/")).concat(fileName));
        return imagePath;
    }

    public Path getUserPicture(String fileName) throws Exception {
        Path imagePath = Paths.get(this.getUploadDirectory("user/").concat(fileName));
        return imagePath;
    }
}
