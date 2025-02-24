import { request, ResponsePageResult, ResponseResult } from '@/service/base';

export async function freemarkerExecute(params: any): ResponseResult {
    return request.post('/v1/tool/freemarkerExecute', params);
}