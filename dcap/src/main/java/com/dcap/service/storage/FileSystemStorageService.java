package com.dcap.service.storage;

import com.dcap.helper.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Class from spring.io tutorial
 */

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocationData;
    private final Path rootLocationPythonCode;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocationData = Paths.get(properties.getLocationForData());
        this.rootLocationPythonCode = Paths.get(properties.getLocationForPythonCode());
        init();
    }

    @Override
    public Pair<String, String> store(MultipartFile file, String type) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path resolve;
        String name = null;
        Path thisLocation;
        if(type=="data"){
            thisLocation = this.rootLocationData;
        }else {
            thisLocation = this.rootLocationPythonCode;
        }
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            resolve = thisLocation.resolve(filename);
            name = filename;
            int ind = 1;
            while (Files.exists(resolve)) {
                Pattern pattern = Pattern.compile("(.*)(\\..*$)");
                Matcher matcher = pattern.matcher(filename);
                name = filename;
                if (matcher.matches()) {
                    name = matcher.group(1) + "(" + ind++ + ")" + matcher.group(2);
                }
                resolve = thisLocation.resolve(name);
            }
            System.out.println(resolve);
            Files.copy(file.getInputStream(), resolve,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return new Pair<>(resolve.toString(), filename);
    }

    @Override
    public String store(MultipartFile file, String path, String type) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path pathForFile;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            pathForFile = Paths.get(path);
            Files.copy(file.getInputStream(), pathForFile.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return pathForFile.toString();
    }

    @Override
    public Stream<Path> loadAll(String type) {
        Path thisLocation;
        if(type=="data"){
            thisLocation = this.rootLocationData;
        }else {
            thisLocation = this.rootLocationPythonCode;
        }

        try {
            return Files.walk(this.rootLocationData, 1)
                    .filter(path -> !path.equals(this.rootLocationData))
                    .map(path -> this.rootLocationData.relativize(path));
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename, String type) {
        Path thisLocation;
        if(type=="data"){
            thisLocation = this.rootLocationData;
        }else {
            thisLocation = this.rootLocationPythonCode;
        }
        return thisLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, String type) {
        try {
            Path file = load(filename, type);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(String type) {
        Path thisLocation;
        if(type=="data"){
            thisLocation = this.rootLocationData;
        }else {
            thisLocation = this.rootLocationPythonCode;
        }
        FileSystemUtils.deleteRecursively(thisLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocationData);
            Files.createDirectories(rootLocationPythonCode);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
