<script setup lang="ts">
import { FlowDefineTable, FlowDefineDrawer, FlowDefineFilter } from './define';
import { flowDefineService, flowVersionService, commonService } from '@/service';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Download, Upload } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import {useGlobalStore} from "@/store/globaleStore.ts";
import { storeToRefs } from 'pinia'
import CopyFlowDrawer from "@/views/flow/define/CopyFlowDrawer.vue";

const router = useRouter();
const globalStore = useGlobalStore();
const { appCode } = storeToRefs(globalStore);

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
const copyDrawerRef = ref();
const selectedRows = ref([]);
const isLoading = ref(false);
const fileList = ref([]);

const filter = ref<{
  flowName?: string;
  flowType?: string;
}>({});

const deployFormVisible = ref(false);
const importFormVisible = ref(false);
let deployForm = reactive({
  flowDefinitionId: '',
  flowName: '',
  flowDeployVersion: '',
  flowVersionRemark: '',
});

// 初始加载
queryFlowDefinePage();

function onSearch(param: typeof filter.value) {
  filter.value = param;
  onPageChange(1);
}

async function queryFlowDefinePage() {
  loading.value = true;
  const res = await flowDefineService.queryFlowDefinePage({
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
  queryFlowDefinePage();
}

function openflowDefineAdd() {
  drawerRef.value.open();
}

async function addFlowDefineItem(row: any) {
  const res = await flowDefineService.addDefineInfo(props.appCode, row);
  if (res.result) {
    ElMessage({ type: 'success', message: '新建成功' });
    await queryFlowDefinePage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

async function updateFlowDefineItem(row: any) {
  const res = await flowDefineService.updateDefineInfo(row);
  if (res.result) {
    ElMessage({ type: 'success', message: '修改成功' });
    await queryFlowDefinePage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

async function copyFlowDefineItem(row: any) {
  const res = await flowDefineService.copyDefineInfo(row);
  if (res.result) {
    ElMessage({ type: 'success', message: '复制成功' });
    await queryFlowDefinePage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openDeployDialog(row: any) {
  deployForm.flowDefinitionId = row.id;
  deployForm.flowName = row.flowName;
  flowVersionService.getLatestDeployVersion(row.flowKey).then(res => {
    deployFormVisible.value = true;
    deployForm.flowDeployVersion = res.result as string;
  });
}

function openImportDialog() {
  fileList.value = [];
  importFormVisible.value = true;
}

async function onSubmitDeploy() {
  await deployFlowDefine(deployForm.flowDefinitionId, deployForm.flowDeployVersion, deployForm.flowVersionRemark);
}

async function deployFlowDefine(flowDefinitionId: string, flowDeployVersion: string, flowVersionRemark: string) {
  const res = await flowDefineService.deployFlowDefine({
    flowDefinitionId: flowDefinitionId,
    flowDeployVersion: flowDeployVersion,
    flowVersionRemark: flowVersionRemark,
  });
  if (res.success) {
    ElMessage({ type: 'success', message: '部署成功' });
    deployFormVisible.value = false;
    await queryFlowDefinePage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openDelete(row: any) {
  ElMessageBox.confirm(`确定删除'${row.flowName}'流程吗?`, '操作确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      deleteFlowDefineItem(row);
    })
    .catch(() => {});
}

async function deleteFlowDefineItem(row: any) {
  const res = await flowDefineService.deleteFlowDefineById(row.id);
  if (res.success) {
    ElMessage({ type: 'success', message: '删除成功' });
    await queryFlowDefinePage();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openEdit(row: any) {
  drawerRef.value.open(row);
}

function openCopy(row: any) {
  copyDrawerRef.value.open(row);
}

function handleSelectionChange(rows) {
  selectedRows.value = rows;
}

async function exportFlowDefine() {
  const ids = selectedRows.value.map(item => item.id);
  if (ids.length === 0) {
    ElMessage.error('请先选择要导出的流程！');
    return;
  }
  await flowDefineService.exportFlowDefine(props.appCode, ids);
}


function handleFileChange(file) {
  fileList.value[0] = file;
}

function beforeUpload() {
  debugger
  fileList.value = [];
}

function handleSuccess() {
  debugger
}

function handleRemove(a,b,c,d) {
  fileList.value = [];
}

async function onImportFlowDefine() {
  if (fileList.value.length === 0) {
    ElMessage.error('请先选择文件！');
    return;
  }
  isLoading.value = true;
  // 创建 FormData 对象并添加文件
  const formData = new FormData();
  formData.append('file', fileList.value[0].raw);  // 'file' 是后端接收的字段
  const success = await flowDefineService.importFlowDefine(props.appCode, formData);
  isLoading.value = false;
  if (success) {
    importFormVisible.value = false;
    await queryFlowDefinePage();
  }
}
</script>

<template>
  <div class="page-flow-define">
    <el-container>
      <el-header class="page-header">
        <FlowDefineFilter @search="onSearch" />
        <el-button :icon="Plus" type="primary" @click="openflowDefineAdd">新建</el-button>
        <el-button :icon="Download" @click="exportFlowDefine">导出</el-button>
        <el-button :icon="Upload" @click="openImportDialog">导入</el-button>
      </el-header>
      <el-main class="page-body">
        <FlowDefineTable
          :dataRows="dataRows"
          :dataTotal="dataTotal"
          :pageNum="pageNum"
          :pageSize="pageSize"
          :loading="loading"
          @pageChange="onPageChange"
          @deploy="openDeployDialog"
          @edit="openEdit"
          @delete="openDelete"
          @copy="openCopy"
          @handleSelectionChange="handleSelectionChange"
        />
      </el-main>
    </el-container>
    <FlowDefineDrawer ref="drawerRef" @add="addFlowDefineItem" @edit="updateFlowDefineItem"/>

    <CopyFlowDrawer ref="copyDrawerRef" @copy="copyFlowDefineItem"/>

    <el-dialog v-model="deployFormVisible" :show-close="false" title="部署流程" width="400">
      <el-form :model="deployForm">
        <el-form-item label="流程名称">
          <el-input v-model="deployForm.flowName" disabled />
        </el-form-item>
        <el-form-item label="部署版本">
          <el-input v-model="deployForm.flowDeployVersion" disabled />
        </el-form-item>
        <el-form-item label="版本描述">
          <el-input type="textarea" v-model="deployForm.flowVersionRemark" maxlength="120" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deployFormVisible = false">取消</el-button>
          <el-button type="primary" @click="onSubmitDeploy">部署</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="importFormVisible" :show-close="false" title="导入流程" width="400">
      <el-upload
          action="/upload-endpoint"
          :on-success="handleSuccess"
          :on-error="handleError"
          :before-upload="beforeUpload"
          :file-list="fileList"
          :show-file-list="true"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleRemove"
          :limit="1"
      >
        <el-button size="small" type="primary">选择文件</el-button>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importFormVisible = false">取消</el-button>
          <el-button type="primary" @click="onImportFlowDefine" :loading="isLoading">上传</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
