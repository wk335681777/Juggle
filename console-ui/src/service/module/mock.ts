import {mockApi} from '@/service/api';

export async function get(id: number) {
  return mockApi.get(id);
}

export async function update(params: Parameters<typeof mockApi.update>[0]) {
  return mockApi.update(params);
}

export async function deleteById(id: number) {
  return mockApi.deleteById(id);
}

export async function pageQuery(params: Parameters<typeof mockApi.pageQuery>[0]) {
  return mockApi.pageQuery(params);
}

export async function add(params: Parameters<typeof mockApi.update>[0]) {
  return mockApi.add(params);
}