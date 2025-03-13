package net.somta.juggle.console.interfaces.param.flow;

import net.somta.core.base.page.PageParam;

/**
 * @author husong
 */
public class FlowTagQueryParam extends PageParam {

    private String code;
    private String appCode;
    private Long ownerId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
