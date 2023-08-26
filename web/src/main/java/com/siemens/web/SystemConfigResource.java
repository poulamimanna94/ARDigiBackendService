package com.siemens.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siemens.publishers.OnAssetFileUploadPublisher;

@RestController
@RequestMapping(URLConstants.URL_API_SYSTEM_CONFIG)
public class SystemConfigResource
{
    private final Path uploadDirPath = Paths.get("uploads");
    
    @Autowired
    private OnAssetFileUploadPublisher assetFileUploadPublisher;
    
    private final Logger log = LoggerFactory.getLogger(SystemConfigResource.class);
    
    @RequestMapping(value = URLConstants.URL_UPLOAD, method = RequestMethod.POST)
    public void save(@RequestPart("file") MultipartFile file, @RequestParam(defaultValue = "no") String fileUse)
    {
        try
        {
            String assetFileName = file.getOriginalFilename();
            Path fileToDeletePath = uploadDirPath.resolve(assetFileName).toAbsolutePath();
            
            // Create the folder if not exists
            if (!Files.exists(uploadDirPath))
            {
                Files.createDirectory(uploadDirPath);
            }
            
            // Delete existing file with same name
            if (!Files.deleteIfExists(fileToDeletePath))
            {
                log.debug("Unable to delete file");
            }
            log.debug("File deleted successfully");
            
            if (fileUse.equals("logo"))
            {
                Files.copy(file.getInputStream(), this.uploadDirPath.resolve("company_logo.png"), StandardCopyOption.REPLACE_EXISTING);
            }
            if (fileUse.equals("assetImport"))
            {
                Files.copy(file.getInputStream(), this.uploadDirPath.resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
                log.info("path for uploaded file : " + assetFileName);
                assetFileUploadPublisher.publishOnUserLogin(fileToDeletePath.toString());
                // fileservice.getSheetFromExcel(fileToDeletePath.toString());
            }
            else
            {
                Files.copy(file.getInputStream(), this.uploadDirPath.resolve(file.getOriginalFilename()));
            }
            
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = URLConstants.URL_LOGO_DOWNLOAD, method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Resource load(HttpServletResponse response) throws RuntimeException
    {
        Resource resource;
        try
        {
            String companyLogoImageName = "company_logo.png";
            Path file = uploadDirPath.resolve(companyLogoImageName);
            resource = new UrlResource(file.toUri());
            response.setHeader("filename", companyLogoImageName);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not download the file. Error: " + e.getMessage());
        }
        
        return resource;
    }
    
    @RequestMapping(value = URLConstants.URL_ASSET_TEMPLATE_DOWNLOAD, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> downloadAssetTemplate(HttpServletResponse response) throws RuntimeException
    {
        String assetTemplatePath = "assets_list.xlsx";
        
        File file = new File("template" + File.separator + assetTemplatePath);
        
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + assetTemplatePath);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try
        {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return ResponseEntity.ok()
            .headers(header)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
        
    }
}
