package net.somta.juggle.console.domain.mock.enums;

import net.somta.core.base.IBaseError;
import net.somta.juggle.common.constants.ApplicationConstants;

/**
 * @author husong
 */
public enum MockErrorEnum implements IBaseError {
    PATH_KEY_EXIST(1000,  "路径已存在"),
    ;

    private int errorCode;
    private String errorMsg;

    MockErrorEnum(int errorCode, String errorMsg) {
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