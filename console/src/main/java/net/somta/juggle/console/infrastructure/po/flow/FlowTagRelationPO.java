package net.somta.juggle.console.infrastructure.po.flow;

import net.somta.core.base.BaseModel;

/**
 * @author Gavin
 */
public class FlowTagRelationPO extends BaseModel {
    private Long id;

    private String appCode;

    private Long tagId;

    private Long flowDefinitionId;

    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getFlowDefinitionId() {
        return flowDefinitionId;
    }

    public void setFlowDefinitionId(Long flowDefinitionId) {
        this.flowDefinitionId = flowDefinitionId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
