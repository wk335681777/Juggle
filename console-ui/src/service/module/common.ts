import { commonAPI } from '@/service/api';
import {ResponseResult} from "@/service/base";
import {ElMessage} from "element-plus";

export async function listDataType() {
  return commonAPI.listDataType();
}

export const dataType = (function () {
  let dataTypeList: any[] = [];
  let pedding: any = null;
  async function getList(useCache: boolean = true) {
    if (!useCache || dataTypeList.length === 0) {
      pedding = pedding || listDataType();
      const res = await pedding;
      if (res.success) {
        pedding = null;
        dataTypeList = res.result;
      }
    }
    return dataTypeList;
  }
  function clearList() {
    dataTypeList = [];
  }
  return {
    getList,
    clearList,
  };
})();


export async function exportFile(url, params): ResponseResult {
  const res = await commonAPI.exportFile(url, params);
  const response = res.error ? res.error?.response : res.response;
  if (res.error) {
    const msg = await blobToText(response.data) || '文件下载失败！'
    ElMessage({ type: 'error', message: msg });
    return;
  }

  // 获取 Content-Disposition 头部信息
  const disposition = response.headers['content-disposition'];
  let fileName = 'default_filename.txt'; // 默认文件名

  // 从 header 中解析出文件名
  if (disposition && disposition.indexOf('attachment') !== -1) {
    const fileNameMatch = disposition.match(/filename="([^"]*)"/);
    if (fileNameMatch && fileNameMatch[1]) {
      fileName = fileNameMatch[1];
    }
  }

  // 创建 Blob 对象，准备下载
  const blob = response.data;
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = fileName;  // 使用从响应头解析出来的文件名
  link.click();
}

function blobToText(blob) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result);  // 成功时返回文本
    reader.onerror = reject;  // 错误时拒绝
    reader.readAsText(blob);  // 读取 blob 数据为文本
  });
}

export async function importFile(url: string, formData: FormData) {
  const res = await commonAPI.importFile(url, formData);
  console.info(res);

  const response = res.error ? res.error?.response : res.response;
  if (res.error) {
    const msg = await blobToText(response.data) || '文件导入失败！'
    ElMessage({ type: 'error', message: msg });
  } else {
    if (response.data.success) {
      ElMessage({ type: 'success', message: '文件导入成功' });
      return true;
    } else {
      ElMessage({ type: 'error', message: '文件导入失败: ' + response.data.errorMsg });
    }
  }
  return false;
}