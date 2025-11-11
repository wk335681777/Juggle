import { request, ResponsePageResult, ResponseResult } from '@/service/base';

export function updateFlowStatus(flowId: number, flowStatus: number): ResponsePageResult {
  return request.put('/v1/flow/status', { flowId: flowId, flowStatus: flowStatus });
}

export async function flowPage(params: { appCode: string, pageNum: number; pageSize: number; flowName?: string; flowType?: string }): ResponsePageResult {
  return request.post('/v1/flow/page', params);
}

export async function deleteFlow(id: number): ResponseResult<boolean> {
  return request.delete(`/v1/flow/delete/${id}`);
}

export async function saveParamsFlow(params: { appCode: string, headers:{ key: string, value: string },body: string}): ResponsePageResult {
  return request.post('/v1/flow/saveParams', params);
}

export async function viewParamsFlow(params: {id: number, pageNum: number, pageSize: number}): ResponsePageResult {
  return request.get('/v1/flow/viewParams', { params });
}

export async function deleteParamsFlow(id: number): ResponseResult<boolean> {
  return request.delete(`/v1/flow/deleteParam/${id}`);
}
