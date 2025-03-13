import type { TagInfo, TagTreeInfo } from '@/typings';
import { request, ResponseResult } from '@/service/base';

export function get(id: number): ResponseResult<TagInfo> {
    return request.get('/v1/flow/tag/info/' + id);
}

export function update(params : TagInfo): ResponseResult {
    return request.post('/v1/flow/tag/update', params);
}

export async function add(mockInfo : TagInfo): ResponseResult {
    return request.post('/v1/flow/tag/add', mockInfo);
}

export async function deleteById(id: number, appCode: string): ResponseResult<boolean> {
    return request.post(`/v1/flow/tag/delete`,{id: id, appCode: appCode});
}

export async function queryTree(params: { appCode: string; code?: string; }): TagTreeInfo {
    return request.post(`/v1/flow/tag/queryTree`, params);
}

