<script setup lang="ts">
import { MockTable, MockDrawer, MockFilter } from './mock';
import { mockService } from '@/service';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps({
  appCode: {
    type: String,
    required: true
  }
});

const pageNum = ref(1);
const pageSize = ref(10);
const dataTotal = ref(0);
const dataRows = ref<Record<string, any>[]>([]);
const loading = ref(false);
const drawerRef = ref();
const filter = ref<{
  flowName?: string;
  flowType?: string;
}>({});

// 初始加载
queryMockPage();

function onSearch(param: typeof filter.value) {
  filter.value = param;
  onPageChange(1);
}

async function queryMockPage() {
  loading.value = true;
  const res = await mockService.pageQuery({
    pageSize: pageSize.value,
    pageNum: pageNum.value,
    appCode: props.appCode,
    ...filter.value,
  });
  if (res.success) {
    dataTotal.value = res.total;
    dataRows.value = res.result;
  }
  loading.value = false;
}

function onPageChange(page: number) {
  pageNum.value = page;
  queryMockPage();
}

function openMockAdd() {
  drawerRef.value.open('add');
}

function openMockView(row: any) {
  drawerRef.value.open('view', row);
}

async function addMockItem(row: any) {
  debugger
  row.appCode = props.appCode;
  const res = await mockService.add(row);
  if (res.success) {
    ElMessage({ type: 'success', message: '新建成功' });
    await queryMockPage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

async function updateMockItem(row: any) {
  const res = await mockService.update(row);
  if (res.success) {
    ElMessage({ type: 'success', message: '修改成功' });
    await queryMockPage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openDelete(row: any) {
  ElMessageBox.confirm(`确定删除'${row.name}'流程吗?`, '操作确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
      .then(() => {
        deleteMockItem(row);
      })
      .catch(() => {});
}

async function deleteMockItem(row: any) {
  const res = await mockService.deleteById(row.id);
  if (res.success) {
    ElMessage({ type: 'success', message: '删除成功' });
    await queryMockPage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openEdit(row: any) {
  drawerRef.value.open('edit', row);
}
</script>

<template>
  <div class="page-flow-define">
    <el-container>
      <el-header class="page-header">
        <MockFilter @search="onSearch" />
        <el-button :icon="Plus" type="primary" @click="openMockAdd">新建</el-button>
      </el-header>
      <el-main class="page-body">
        <MockTable
            :dataRows="dataRows"
            :dataTotal="dataTotal"
            :pageNum="pageNum"
            :pageSize="pageSize"
            :loading="loading"
            @pageChange="onPageChange"
            @view="openMockView"
            @edit="openEdit"
            @delete="openDelete"
        />
      </el-main>
    </el-container>
    <MockDrawer ref="drawerRef" @add="addMockItem" @edit="updateMockItem" />
  </div>
</template>
