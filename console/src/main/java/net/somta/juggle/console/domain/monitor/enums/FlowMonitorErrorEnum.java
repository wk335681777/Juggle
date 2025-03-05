package net.somta.juggle.console.domain.monitor.enums;

import net.somta.core.base.IBaseError;
import net.somta.juggle.common.constants.ApplicationConstants;

/**
 * @author husong
 */
public enum FlowMonitorErrorEnum implements IBaseError {
    CONNECT_CAMEL_JMX_ERROR(1000,  "连接camel jmx异常"),
    QUERY_CAMEL_JMX_ERROR(2000,  "查询camel jmx异常"),
    ;

    private int errorCode;
    private String errorMsg;

    FlowMonitorErrorEnum(int errorCode, String errorMsg) {
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