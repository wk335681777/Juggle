import {appApi} from '@/service/api';

export async function get(id: number) {
  return appApi.get(id);
}

export async function update(params: Parameters<typeof appApi.update>[0]) {
  return appApi.update(params);
}

export async function deleteById(id: number) {
  return appApi.deleteById(id);
}

export async function list(params: Parameters<typeof appApi.update>[0]) {
  return appApi.list(params);
}

export async function add(params: Parameters<typeof appApi.update>[0]) {
  return appApi.add(params);
}