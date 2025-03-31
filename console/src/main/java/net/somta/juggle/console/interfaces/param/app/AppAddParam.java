package net.somta.juggle.console.interfaces.param.app;

import jakarta.validation.constraints.NotBlank;
import net.somta.core.base.BaseModel;

/**
 * @author husong
 * @since 1.0.0
 */
public class AppAddParam {

    @NotBlank
    private String appName;
    @NotBlank
    private String appCode;

    private String remark;

    private Integer deleted;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
