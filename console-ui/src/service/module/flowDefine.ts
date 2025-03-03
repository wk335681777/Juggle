import {flowDefineAPI} from '@/service/api';
import { commonService } from '@/service';

export async function addDefineInfo(appCode: string, params: Parameters<typeof flowDefineAPI.addDefineInfo>[1]) {
  return flowDefineAPI.addDefineInfo(appCode, params);
}

export async function getDefineInfo(id: number) {
  return flowDefineAPI.getDefineInfo(id);
}

export async function getDebugInfo(id: number) {
  return flowDefineAPI.getDebugInfo(id);
}

export async function queryFlowDefinePage(params: Parameters<typeof flowDefineAPI.flowDefinePage>[0]) {
  return flowDefineAPI.flowDefinePage(params);
}

export async function deleteFlowDefineById(params: Parameters<typeof flowDefineAPI.deleteFlowDefine>[0]) {
  return flowDefineAPI.deleteFlowDefine(params);
}

export async function updateDefineInfo(params: Parameters<typeof flowDefineAPI.updateDefineInfo>[0]) {
  return flowDefineAPI.updateDefineInfo(params);
}

export async function copyDefineInfo(flowDefinitionCopyParam: Parameters<typeof flowDefineAPI.copyDefineInfo>[0]) {
  return flowDefineAPI.copyDefineInfo(flowDefinitionCopyParam);
}

export async function draftDefineInfo(appCode: string,id: number,flowKey: string ) {
  return flowDefineAPI.draftDefineInfo(appCode,id,flowKey);
}

export async function saveFlowContent(params: Parameters<typeof flowDefineAPI.saveFlowContent>[0]) {
  return flowDefineAPI.saveFlowContent(params);
}

export async function deployFlowDefine(params: Parameters<typeof flowDefineAPI.deployFlowDefine>[0]) {
  return flowDefineAPI.deployFlowDefine(params);
}

export async function debugFlow(flowKey: string, params: Parameters<typeof flowDefineAPI.debugFlow>[1]) {
  return flowDefineAPI.debugFlow(flowKey, params);
}

export async function exportFlowDefine(appCode: string, idList:[]) {
  return commonService.exportFile("v1/flow/definition/export", { appCode: appCode, idList: idList});
}

export async function importFlowDefine(appCode: string, formData: FormData) {
  return commonService.importFile(`v1/flow/definition/${appCode}/import`, formData);
}