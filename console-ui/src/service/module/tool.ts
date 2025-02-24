import {toolApi} from '@/service/api';

export async function freemarkerExecute(params: any) {
    return toolApi.freemarkerExecute(params);
}