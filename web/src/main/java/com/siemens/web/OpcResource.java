package com.siemens.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.siemens.config.ApiResponse;
import com.siemens.constant.Constants;
import com.siemens.domain.OpcUaConnector;
import com.siemens.service.OpcService;

/**
 * OpcResource controller
 */
@RestController
@RequestMapping(URLConstants.URL_API_OPC_ROOT)
public class OpcResource
{
    @Autowired
    private OpcService opcService;
    
    String result;
    
    private final Logger log = LoggerFactory.getLogger(OpcResource.class);
    
    /**
     * POST save config & connect to Node server
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * 
     */
    @PostMapping("/connect-node")
    public ResponseEntity<ApiResponse<String>> connectNodeSrvr(@RequestBody OpcUaConnector opc)
        throws JsonParseException, JsonMappingException, IOException
    {
        log.info("OpcResource: inside connectNodeSrvr() method");
        
        result = opcService.connectNode(opc);
        
        log.info("OpcResource: result :" + result);
        
        if (Constants.NODE_SERVER_ERROR.equalsIgnoreCase(result))
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error at server end..", result),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully modified record.", result),
            HttpStatus.OK);
    }
    
    /**
     * {@code GET  /config} : get opc-config.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and config in body.
     */
    @GetMapping("/get-config")
    public OpcUaConnector getConfig()
    {
        log.info("OpcResource: inside getConfig() method");
        log.info("OpcResource: result :" + opcService.fetchConfig().get());
        
        return opcService.fetchConfig().get();
    }
    
    /**
     * {@code GET  /config} : get tree data from node server .
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and config in body.
     */
    @GetMapping("/node-tree-data")
    public ResponseEntity<ApiResponse<String>> rebuildTree(@RequestParam String rebuildTree)
    {
        log.info("OpcResource: inside buildNodeTree() method .. rebuildTree :" + rebuildTree);
        ApiResponse<String> result;
        
        if ("true".equalsIgnoreCase(rebuildTree))
        {
            // call createTree // deletefile
            result = opcService.callNodeCreateTree();
            log.info("OpcResource: rebuildTree result :" + result);
            
            if (result.getStatus() == Constants.HTTP_SERVER_ERROR_CODE)
            {
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else
        {
            log.info("OpcResource: rebuildTree False case:");
            
            // if (Files.exists(Paths.get(null))) {}
            
            // File f = new File("opc-res/opc-tree.json");
            // boolean fileExists = f.exists() && f.isFile();
            // log.info("fileExists :"+fileExists);
            
            // if(fileExists)
            // {
            // log.info("OpcResource: inside getTreeFromConfig() method .. ");
            // String treeConf;
            //
            // treeConf = opcService.getTreeFromConfig();
            // //log.info("OpcResource: getTreeFromConfig() result :"+treeConf);
            // return new ResponseEntity<>(
            // new ApiResponse<String>(HttpStatus.OK.value(), "Successfully fetched record.", treeConf),
            // HttpStatus.OK);
            // }
            // else
            // {
            result = opcService.getTreeFromNodeSrvr();
            log.info("OpcResource: getTreeFromNodeSrvr() result :" + result);
            
            if (result.getStatus() == Constants.HTTP_SERVER_ERROR_CODE)
            {
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
            // }
        }
    }
}
