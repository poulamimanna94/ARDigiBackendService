/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.service;

import java.net.URI;

import javax.annotation.concurrent.NotThreadSafe;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

@NotThreadSafe
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase
{
    public static final String METHOD_NAME = "DELETE";
    
    public String getMethod()
    {
        return METHOD_NAME;
    }
    
    public HttpDeleteWithBody(final String uri)
    {
        super();
        setURI(URI.create(uri));
    }
    
    public HttpDeleteWithBody(final URI uri)
    {
        super();
        setURI(uri);
    }
    
    public HttpDeleteWithBody()
    {
        super();
    }
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
