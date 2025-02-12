package net.somta.juggle.console.domain.app.enums;

import net.somta.core.base.IBaseError;
import net.somta.juggle.common.constants.ApplicationConstants;

/**
 * @author husong
 */
public enum AppErrorEnum implements IBaseError {
    APP_KEY_EXIST(1000,  "AppCode已存在"),
    ;

    private int errorCode;
    private String errorMsg;

    AppErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public long getErrorCode() {
        return ApplicationConstants.APP_CODE + errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}