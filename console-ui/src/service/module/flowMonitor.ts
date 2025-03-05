import { flowMonitorApi } from '@/service/api';

export async function pageQuery(params: Parameters<typeof flowMonitorApi.pageQuery>[0]) {
    return flowMonitorApi.pageQuery(params);
}

export async function queryServerIpList() {
    return flowMonitorApi.queryServerIpList();
}