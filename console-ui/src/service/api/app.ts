import { request, ResponsePageResult, ResponseResult } from '@/service/base';

export function get(id: number): ResponseResult {
  return request.get('/v1/app/info/' + id);
}

export function update(params: { id: number, app_name: string, app_code: string, remark?: string }): ResponseResult {
  return request.put('/v1/app/update', params);
}

export async function add(params: { app_name: string, app_code: string, remark?: string }): ResponseResult {
  return request.post('/v1/app/add', params);
}

export async function deleteById(id: number): ResponseResult<boolean> {
  return request.delete(`/v1/app/delete/${id}`);
}

export async function list(params: { app_name?: string, app_code?: string }): ResponseResult<boolean> {
  return request.post('/v1/app/list', params);
}

