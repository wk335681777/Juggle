package net.somta.juggle.console.infrastructure.po;

import net.somta.core.base.BaseModel;

/**
 * @author husong
 * @since 1.0.0
 */
public class MockPO extends BaseModel {

    private Long id;
    private String name;
    private String mockKey;
    private String appCode;
    private String remark;
    private String url;
    private String conditions;
    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getMockKey() {
        return mockKey;
    }

    public void setMockKey(String mockKey) {
        this.mockKey = mockKey;
    }
}
