package com.siemens.exception;

import com.siemens.constant.ErrorConstants;

public class UsernameAlreadyUsedException extends BadRequestAlertException
{
    
    private static final long serialVersionUID = 1L;
    
    public UsernameAlreadyUsedException()
    {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Username already used!", "userManagement", "userexists");
    }
}
