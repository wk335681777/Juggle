package net.somta.juggle.console.domain.flow.definition.enums;

import net.somta.core.base.IBaseError;
import net.somta.juggle.common.constants.ApplicationConstants;

/**
 * @author husong
 */
public enum FlowTagErrorEnum implements IBaseError {
    FLOW_TAG_CODE_EXIST_ERROR(1000,"流程标签编码已存在");

    private int errorCode;
    private String errorMsg;

    FlowTagErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public long getErrorCode() {
        return ApplicationConstants.FLOW_DEFINITION_CODE + errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }


}
