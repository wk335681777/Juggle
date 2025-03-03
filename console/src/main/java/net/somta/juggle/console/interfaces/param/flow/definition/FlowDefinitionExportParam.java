package net.somta.juggle.console.interfaces.param.flow.definition;

import java.util.List;

/**
 * @author Gavin
 */
public class FlowDefinitionExportParam {
    private List<Long> idList;
    private String appCode;

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
