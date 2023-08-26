package com.siemens.service;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.siemens.config.ApiResponse;
import com.siemens.domain.OpcUaConnector;

/**
 * Service Interface for managing {@link OpcUaConnector}.
 */
public interface OpcService
{
    
    /**
     * Save config.
     *
     * @param the entity to save.
     * @return the persisted entity.
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    String save(OpcUaConnector opc) throws JsonGenerationException, JsonMappingException, IOException;
    
    String connectNode(OpcUaConnector opc);
    
    Optional<OpcUaConnector> fetchConfig();
    
    ApiResponse<String> getTreeFromNodeSrvr();
    
    String getTreeFromConfig();
    
    ApiResponse<String> callNodeCreateTree();
}
