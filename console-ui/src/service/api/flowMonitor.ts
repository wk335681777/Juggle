import type { MockInfo } from '@/typings';
import { request, ResponsePageResult, ResponseResult } from '@/service/base';


export async function pageQuery(params: { appCode: string; contextType: string; pageNum: number; pageSize: number;
    name?: string; flowKey?: string; ip: String }): ResponsePageResult {
    return request.post(`/v1/flowMonitor/page`, params);
}

export async function queryServerIpList(): ResponseResult {
    return request.get(`/v1/flowMonitor/serverIpList`);
}

