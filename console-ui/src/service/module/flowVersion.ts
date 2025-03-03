import {flowVersionAPI} from '@/service/api';


export async function getLatestDeployVersion(flowId: number) {
  return flowVersionAPI.getLatestDeployVersion(flowId);
}

export async function updateFlowVersionStatus(flowVersionId: number, flowVersionStatus: number) {
  return flowVersionAPI.updateFlowVersionStatus(flowVersionId, flowVersionStatus);
}

export async function queryFlowVersionPage(params: Parameters<typeof flowVersionAPI.flowVersionPage>[0]) {
  return flowVersionAPI.flowVersionPage(params);
}

export async function deleteFlowVersionById(params: Parameters<typeof flowVersionAPI.deleteFlowVersion>[0]) {
  return flowVersionAPI.deleteFlowVersion(params);
}

export async function getFlowDefinition(flowVersionId: number) {
  return flowVersionAPI.getFlowDefinition(flowVersionId);
}


export async function getAsyncFlowResult(flowInstanceId: string) {
  return flowVersionAPI.getAsyncFlowResult(flowInstanceId);
}

export async function versionFlowRestore(appCode: string,flowKey: string,flowVersionId:number) {
  return flowVersionAPI.versionFlowRestore(appCode,flowKey,flowVersionId);
}