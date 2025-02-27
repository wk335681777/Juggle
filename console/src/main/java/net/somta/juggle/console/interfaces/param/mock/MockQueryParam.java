package net.somta.juggle.console.interfaces.param.mock;

import net.somta.core.base.page.PageParam;

/**
 * @author husong
 */
public class MockQueryParam extends PageParam {

    private String name;
    private String url;
    private String appCode;
    private String mockKey;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMockKey() {
        return mockKey;
    }

    public void setMockKey(String mockKey) {
        this.mockKey = mockKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
