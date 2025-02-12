package net.somta.juggle.console.interfaces.param.app;

import net.somta.core.base.page.PageParam;

/**
 * @author husong
 */
public class AppQueryParam extends PageParam {

    private String appName;
    private String appCode;

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
}
