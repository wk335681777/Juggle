import { flowTagApi } from '@/service/api';

export async function get(id: number) {
  return flowTagApi.get(id);
}

export async function add(params: Parameters<typeof flowTagApi.update>[0]) {
  return flowTagApi.add(params);
}

export async function update(params: Parameters<typeof flowTagApi.update>[0]) {
  return flowTagApi.update(params);
}

export async function deleteById(id: number, appCode: string) {
  return flowTagApi.deleteById(id, appCode);
}

export async function queryTree(params: Parameters<typeof mockApi.pageQuery>[0]) {
  return flowTagApi.queryTree(params);
}
