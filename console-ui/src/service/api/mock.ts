import type { MockInfo } from '@/typings';
import { request, ResponsePageResult, ResponseResult } from '@/service/base';

export function get(id: number): ResponseResult<MockInfo> {
    return request.get('/v1/mock/info/' + id);
}

export function update(params : MockInfo): ResponseResult {
    return request.post('/v1/mock/update', params);
}

export async function add(mockInfo : MockInfo): ResponseResult {
    return request.post('/v1/mock/add', mockInfo);
}

export async function deleteById(id: number): ResponseResult<boolean> {
    return request.delete(`/v1/mock/delete/${id}`);
}

export async function pageQuery(params: { appCode: string; pageNum: number; pageSize: number; name?: string; }): ResponsePageResult {
    return request.post(`/v1/mock/page`, params);
}

