package net.somta.juggle.console.interfaces.dto.flow;

import java.util.ArrayList;
import java.util.List;

public class DeployDTO {
    private boolean success;
    private List<DeployDetail> deployDetailList;

    public void addDeployDetail(DeployDetail deployDetail) {
        if (deployDetailList == null) {
            deployDetailList = new ArrayList<DeployDetail>();
        }

        deployDetailList.add(deployDetail);
    }

    public String getMessage() {
        if (deployDetailList == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (DeployDetail deployDetail : deployDetailList) {
            if (deployDetail.isSuccess()) {
                continue;
            }

            sb.append(deployDetail.getServerIp()).append(":").append(deployDetail.getErrorMsg()).append(";\r\n");
        }

        return sb.toString();
    }

    public static class DeployDetail {
        private boolean success;
        private String errorMsg;
        private String serverIp;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DeployDetail> getDeployDetailList() {
        return deployDetailList;
    }

    public void setDeployDetailList(List<DeployDetail> deployDetailList) {
        this.deployDetailList = deployDetailList;
    }
}
