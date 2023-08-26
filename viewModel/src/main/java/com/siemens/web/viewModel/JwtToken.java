/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.web.viewModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtToken
{
    private String idToken;
    
    public JwtToken(String idToken)
    {
        this.idToken = idToken;
    }
    
    @JsonProperty("id_token")
    public String getIdToken()
    {
        return idToken;
    }
    
    public void setIdToken(String idToken)
    {
        this.idToken = idToken;
    }
    
    @Override
    public String toString()
    {
        return "JwtToken [idToken=" + idToken + "]";
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
