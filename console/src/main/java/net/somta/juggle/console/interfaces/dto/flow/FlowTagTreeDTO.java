package net.somta.juggle.console.interfaces.dto.flow;

import java.util.ArrayList;
import java.util.List;

public class FlowTagTreeDTO {

    private Long id;
    private String name;
    private String appCode;
    private String code;
    private String parentCode;
    private String path;
    private String accessType;
    private List<FlowTagTreeDTO> children;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FlowTagTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<FlowTagTreeDTO> children) {
        this.children = children;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public void addChildren(List<FlowTagTreeDTO> children) {
        if (this.children == null) {
            this.children = new ArrayList<FlowTagTreeDTO>();
        }

        this.children.addAll(children);
    }
}
