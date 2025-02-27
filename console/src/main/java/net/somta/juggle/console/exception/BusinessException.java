package net.somta.juggle.console.exception;

import net.somta.juggle.console.domain.flow.definition.enums.FlowDefinitionErrorEnum;

public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(FlowDefinitionErrorEnum errorEnum) {
        super(errorEnum.getErrorMsg());
        this.code = (int) errorEnum.getErrorCode();
        this.message = errorEnum.getErrorMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
