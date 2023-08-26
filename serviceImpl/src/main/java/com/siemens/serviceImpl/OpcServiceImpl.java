package com.siemens.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.config.ApiResponse;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.constant.Constants;
import com.siemens.constant.KeyCloakConstants;
import com.siemens.domain.Asset;
import com.siemens.domain.OpcUaConnector;
import com.siemens.service.OpcService;

/**
 * Service Implementation for managing {@link Asset}.
 */
@Service
public class OpcServiceImpl implements OpcService
{
    
    private final Logger log = LoggerFactory.getLogger(OpcServiceImpl.class);
    
    private static final String OPC_TREE_FILEPATH = "opc-res/opc-tree.json";
    private static final String OPC_CONFIG_FILEPATH = "opc-res/opc-config.json";
    
    ObjectMapper objectMapper;
    
    // @Value(Constants.API_OPC_CONNECT_BASEURL)
    StringBuilder adminUri;
    
    @Value(Constants.API_OPC_CONNECT_BASEURL)
    String baseUrl;
    
    @Value(Constants.API_OPC_ENDPOINT_URI_PROTOCOL)
    StringBuilder opcTcp;
    
    @Autowired
    OpcUaConnector opcObj;
    
    private final Path root = Paths.get("opc-res");
    
    public OpcServiceImpl()
    {
        objectMapper = new ObjectMapper();
    }
    
    // only for test
    @Override
    public String save(OpcUaConnector opc)
        throws JsonGenerationException, JsonMappingException, IOException
    {
        log.info("Request to save Opc config : {}");
        
        opcTcp.setLength(0);
        opcTcp.append("opc.tcp://");
        
        StringBuilder endPoint = opc.formEndPoint(opcTcp, opc.getOpcServerAddress(), opc.getOpcPortNumber(), opc.getOpcServerName());
        opc.setEndPoint(endPoint);
        
        if (!Files.exists(root))
        {
            Files.createDirectory(root);
        }
        objectMapper.writeValue(new File(OPC_CONFIG_FILEPATH), opc);
        
        return "SUCCESS";
    }
    
    public String connectNode(OpcUaConnector opc)
    {
        log.info("--- inside connectNode() ---");
        
        log.info("--- adminUri 1: " + adminUri);
        
        // setting length 0 to avoid url append issue
        adminUri = new StringBuilder();
        adminUri.append(baseUrl);
        
        String postUserReqParam = null;
        HttpResponse response = null;
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(Constants.API_OPC_CONNECT_URL);
        
        log.info("--- adminUri 2: " + adminUri);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPost post = getPostOPCApi(builder.build());
            
            opcTcp.setLength(0);
            opcTcp.append(Constants.API_OPC_ENDPOINT_URI_PROTOCOL);
            
            StringBuilder endPoint = opc.formEndPoint(opcTcp, opc.getOpcServerAddress(), opc.getOpcPortNumber(), opc.getOpcServerName());
            opc.setEndPoint(endPoint);
            
            if (!Files.exists(root))
            {
                Files.createDirectory(root);
            }
            objectMapper.writeValue(new File(OPC_CONFIG_FILEPATH), opc);
            
            postUserReqParam = objectMapper.writeValueAsString(opc);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            log.info("--- connectNode() stringEntity:" + stringEntity);
            
            post.setEntity(stringEntity);
            log.info("--- connectNode() post:" + post);
            
            response = invokePostOPCApi(post);
            int httpStatus = response.getStatusLine().getStatusCode();
            log.info("--- connectNode() httpStatus:" + httpStatus);
            
            callNodeCreateTree();
            if (Constants.HTTP_SUCCESS_CODE == httpStatus)
            {
                return postUserReqParam;
            }
            else
            {
                return Constants.NODE_SERVER_ERROR;
            }
            
        }
        catch (URISyntaxException | IOException e)
        {
            log.error(e.getMessage());
        }
        
        return postUserReqParam;
    }
    
    public HttpResponse invokePostOPCApi(HttpPost post)
    {
        HttpResponse response = null;
        HttpClient client = HttpClientBuilder.create().build();
        
        try
        {
            response = client.execute(post);
            log.info("--- invokePostOPCApi() response:" + response);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    public HttpPost getPostOPCApi(URI uri)
    {
        
        HttpPost post = new HttpPost(uri);
        post.setHeader(AuthoritiesConstants.USER_AGENT,
            AuthoritiesConstants.MOZILLA_WINDOWS_CHROME_SAFARI);
        
        return post;
    }
    
    // get-config
    @Override
    public Optional<OpcUaConnector> fetchConfig()
    {
        try
        {
            opcObj = objectMapper.readValue(new File(OPC_CONFIG_FILEPATH), OpcUaConnector.class);
        }
        catch (IOException e)
        {
            log.debug(e.getMessage());
        }
        return Optional.of(opcObj);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ApiResponse<String> getTreeFromNodeSrvr()
    {
        log.info("Inside getTreeFromNodeSrvr() :");
        
        adminUri = new StringBuilder();
        adminUri.append(baseUrl);
        
        HttpResponse response;
        
        ApiResponse<String> tagList = null;
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(Constants.API_OPC_GET_TREE);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            response = invokeGetOPCApi(builder.build());
            log.info("response from invokeGetOPCApi() :" + response);
            
            InputStream reqContent;
            reqContent = response.getEntity().getContent();
            ObjectMapper mapper = new ObjectMapper();
            tagList = mapper.readValue(reqContent, ApiResponse.class);
            log.info("tagList :" + tagList);
            
            if (!Files.exists(root))
            {
                Files.createDirectory(root);
            }
            
            if ((Constants.HTTP_SERVER_ERROR_CODE) == tagList.getStatus())
            {
                log.info("Status code {} from Node :" + tagList.getStatus());
                return tagList;
            }
            else
            {
                log.info("Status code {} from Node :" + tagList.getStatus());
                objectMapper.writeValue(new File(OPC_TREE_FILEPATH), tagList.getResult());
            }
            
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return tagList;
    }
    
    public HttpResponse invokeGetOPCApi(URI uri)
    {
        HttpResponse response = null;
        
        try
        {
            HttpClient client = HttpClientBuilder.create().build();
            
            HttpGet get = new HttpGet(uri);
            get.setHeader(AuthoritiesConstants.USER_AGENT,
                KeyCloakConstants.USER_AGENTS);
            
            response = client.execute(get);
        }
        catch (IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return response;
    }
    
    @Override
    public String getTreeFromConfig()
    {
        log.info("Inside getTreeFromConfig()--");
        
        String treeConfig = null;
        Path path = Paths.get(OPC_TREE_FILEPATH);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        try
        {
            treeConfig = Files.readString(path);
            
            JsonParser parser = factory.createParser(new File(OPC_TREE_FILEPATH));
            JsonNode actualObj = mapper.readTree(parser);
            log.info("jsonObject :" + actualObj);
            
        }
        catch (IOException e)
        {
            log.debug(e.getMessage());
        }
        return treeConfig;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ApiResponse<String> callNodeCreateTree()
    {
        log.info("Inside callNodeCreateTree() :");
        
        adminUri = new StringBuilder();
        adminUri.append(baseUrl);
        
        HttpResponse response;
        
        ApiResponse<String> apiRespObj = null;
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(Constants.API_OPC_CREATE_TREE);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            response = invokeGetOPCApi(builder.build());
            log.info("response from invokeGetOPCApi() :" + response);
            
            InputStream reqContent;
            reqContent = response.getEntity().getContent();
            ObjectMapper mapper = new ObjectMapper();
            apiRespObj = mapper.readValue(reqContent, ApiResponse.class);
            log.info("apiRespObj :" + apiRespObj);
            
            // adding since if file deleted already or not there then null ptr exception
            File f = new File(OPC_TREE_FILEPATH);
            boolean fileExists = f.exists() && f.isFile();
            log.info("fileExists :" + fileExists);
            
            if (Files.exists(root) && true == fileExists)
            {
                Path fileToDeletePath = Paths.get(OPC_TREE_FILEPATH);
                Files.delete(fileToDeletePath);
            }
            
            log.info("Status code from Node :" + apiRespObj.getStatus());
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.debug(e.getMessage());
        }
        return apiRespObj;
    }
}
