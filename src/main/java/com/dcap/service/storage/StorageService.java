package com.dcap.service.storage;

import com.dcap.helper.Pair;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
/**
 * Class from spring.io tutorial
 */
public interface StorageService {

    void init();

    Pair<String, String> store(MultipartFile file, String type);

    String store(MultipartFile file, String path, String type);

    Stream<Path> loadAll(String type);

    Path load(String filename, String type);

    Resource loadAsResource(String filename, String type);

    void deleteAll(String type);

}
