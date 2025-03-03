import { request, ResponseResult } from '@/service/base';

export function listDataType(): ResponseResult {
  return request.get('/v1/dataType/list');
}

export function exportFile(url: string, params: any) {
  return request.post(url, params, { responseType: 'blob' });
}

export function importFile(url: string, formData: FormData) {
  return request.post(url, formData, { headers: { 'Content-Type': 'multipart/form-data' }});
}
