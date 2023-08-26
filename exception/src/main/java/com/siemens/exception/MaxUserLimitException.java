/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.exception;

import com.siemens.constant.ErrorConstants;

public class MaxUserLimitException extends BadRequestAlertException
{
    
    private static final long serialVersionUID = 1L;
    
    public MaxUserLimitException()
    {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Reached Max User Limit", "userManagement", "maxUserLimit");
    }
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
