package net.somta.juggle.console.domain.flow.flowinfo.enums;

/**
 * @author husong
 */
public enum FlowTypeEnum {
    SINGLE_VERSION("single","单流程"),
    MULTI_VERSION("multi","多流程");

    private String code;
    private String desc;

    FlowTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
