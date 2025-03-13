<script setup lang="ts">
import { FlowDefineTable, FlowDefineDrawer, FlowDefineFilter } from './define';
import { flowDefineService, flowVersionService, flowTagService } from '@/service';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Download, Upload } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import {useGlobalStore} from "@/store/globaleStore.ts";
import { storeToRefs } from 'pinia'
import CopyFlowDrawer from "@/views/flow/define/CopyFlowDrawer.vue";
import FlowTag from "@/views/flow/FlowTag.vue";

const router = useRouter();
const globalStore = useGlobalStore();

const props = defineProps({
  appCode: {
    type: String,
    required: true
  }
});

const pageNum = ref(1);
const pageSize = ref(5);
const dataTotal = ref(0);
const dataRows = ref<Record<string, any>[]>([]);
const loading = ref(false);
const drawerRef = ref();
const copyDrawerRef = ref();
const selectedRows = ref([]);
const isLoading = ref(false);
const fileList = ref([]);
const selectedNode = ref({});

const filter = ref<{
  flowName?: string;
  flowType?: string;
}>({});

const deployFormVisible = ref(false);
const importFormVisible = ref(false);
const tagFormVisible = ref(false);

let deployForm = reactive({
  flowDefinitionId: '',
  flowName: '',
  flowDeployVersion: '',
  flowVersionRemark: '',
});

function getDefaultTagForm() {
  return {
    id: null,
    name: '',
    code: '',
    parentCode: '',
    path: '',
    appCode: '',
    accessType: '',
  };
}
let tagForm = reactive(getDefaultTagForm());

initPage();

function initPage() {
  queryFlowDefinePage();
  queryTagTree();
}

// 初始加载
// queryFlowDefinePage();

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
    tagPath: selectedNode.value.path,
    ...filter.value,
  });
  if (res.success) {
    dataTotal.value = res.total;
    dataRows.value = res.result;
  }
  loading.value = false;
}

async function queryTagTree() {
  const res = await flowTagService.queryTree({
    appCode: props.appCode
  });
  if (res.success) {
    treeData.value = res.result;
  }
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

function openDraft(row: any) {
  ElMessageBox.confirm(`确定放弃本次'${row.flowName}'的草稿吗?`, '操作确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
      .then(() => {
        draftFlowDefineItem(row);
      })
      .catch(() => {});
}

async function draftFlowDefineItem(row: any) {
  const res = await flowDefineService.draftDefineInfo(row.appCode,row.id,row.flowKey);
  if (res.success) {
    ElMessage({ type: 'success', message: '操作成功' });
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
  fileList.value = [];
}

function handleSuccess() {
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

const treeData = ref([]);
const defaultProps = ref({
  children: "children",
  label: "name",
});

const menuOp = reactive({
  // 右键菜单相关
  menuVisible: false,
  menuX: 0,
  menuY: 0,
  selectedNode: null,
});
const menuVisible = ref(true);
const menuX = ref(0);
const menuY = ref(0);
// const contextMenu = ref(null);
const dropdown = ref();


// 处理点击树节点的事件
function handleNodeClick(node) {
  console.log("选择的分类:", node);
  selectedNode.value = node;
  queryFlowDefinePage();
}

// 右键菜单事件
function handleRightClick(event, data, node) {
  event.preventDefault(); // 阻止默认右键菜单
  menuOp.selectedNode = { data, node };
  menuOp.menuVisible = true;
  menuOp.menuX = event.clientX;
  menuOp.menuY = event.clientY;
  dropdown.value.handleOpen();

  // 点击其他地方隐藏菜单
  document.addEventListener("click", hideContextMenu);
  document.addEventListener("contextmenu", hideContextMenu);

}

function hideContextMenu(event) {
  menuOp.menuVisible = false;
  document.removeEventListener("click", hideContextMenu);
}

function openAddTagDialog() {
  debugger
  tagFormVisible.value = true;
  Object.assign(tagForm, getDefaultTagForm());
  const { code, path, appCode, accessType } = menuOp.selectedNode.data;
  Object.assign(tagForm, { parentCode: code, path, appCode, accessType });
}

function openEditTagDialog() {
  debugger
  if (!menuOp.selectedNode?.data) return; // 避免空对象错误
  const { id, name, code, parentCode, path } = menuOp.selectedNode.data;
  Object.assign(tagForm, { id, name, code, parentCode, path });
  tagFormVisible.value = true;
}

function closeTagDialog() {
  tagFormVisible.value = false;
}

async function onSubmitTag() {
  const params = { ...tagForm }
  params.path = tagForm.path + '/' + tagForm.code;
  let res;
  if (params.id) {
    res = await flowTagService.update(params);
  } else {
    res = await flowTagService.add(params);
  }

  if (res.success) {
    ElMessage({ type: 'success', message: '保存成功' });
    tagFormVisible.value = false;
    await queryTagTree();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

function openDeleteTag() {
  const { name } = menuOp.selectedNode.data;
  ElMessageBox.confirm(`确定删除'${name}'标签吗?`, '操作确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
      .then(() => {
        deleteTag();
      })
      .catch(() => {});
}

async function deleteTag() {
  const { id, appCode } = menuOp.selectedNode.data;
  const res = await flowTagService.deleteById(id, appCode);
  if (res.success) {
    ElMessage({ type: 'success', message: '删除成功' });
    queryTagTree();
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

async function refreshTag() {
  queryTagTree();
}

</script>

<template>
  <el-container style="height: 80vh;">
    <el-aside width="200px" class="aside">
      <el-tree
          :data="treeData"
          :props="defaultProps"
          highlight-current
          default-expand-all
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
          @node-contextmenu="handleRightClick"
          class="custom-tree">
      </el-tree>
      <el-dropdown
          v-show="true"
          ref="dropdown"
          trigger="contextmenu"
          @command="handleCommand"
          :style="{ left: menuOp.menuX + 'px', top: menuOp.menuY + 'px', position: 'absolute' }"
      >
        <span></span>
        <template #dropdown>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click="openAddTagDialog">新增标签</el-dropdown-item>
            <el-dropdown-item v-if="menuOp.selectedNode.node.level !== 1" @click.native="openEditTagDialog">编辑标签</el-dropdown-item>
            <el-dropdown-item v-if="menuOp.selectedNode.node.level !== 1" @click.native="openDeleteTag">删除标签</el-dropdown-item>
            <el-dropdown-item @click.native="refreshTag">刷新</el-dropdown-item>

          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-aside>
    <el-main >
      <div class="page-flow-define" >
        <el-container style="height: 80vh;">
          <el-header class="page-header">
            <FlowDefineFilter @search="onSearch" />
            <el-button :icon="Plus" type="primary" @click="openflowDefineAdd">新建</el-button>
            <el-button :icon="Download" @click="exportFlowDefine">导出</el-button>
            <el-button :icon="Upload" @click="openImportDialog">导入</el-button>
          </el-header>
          <el-main class="page-body" >
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
                @draft="openDraft"
            />
          </el-main>
        </el-container>
        <FlowDefineDrawer ref="drawerRef" :appCode="appCode" @add="addFlowDefineItem" @edit="updateFlowDefineItem"/>

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

        <el-dialog v-model="tagFormVisible" :show-close="false" title="标签" width="400">
          <el-form :model="deployForm">
            <el-form-item v-if="tagForm.id == null" label="父级标签" required>
              <el-text>{{menuOp.selectedNode.data.name}}</el-text>
            </el-form-item>
            <el-form-item label="流程名称" required>
              <el-input v-model="tagForm.name"/>
            </el-form-item>
            <el-form-item label="标签编码" required>
              <el-input v-model="tagForm.code" :disabled="tagForm.id != null"/>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="closeTagDialog">取消</el-button>
              <el-button type="primary" @click="onSubmitTag">提交</el-button>
            </span>
          </template>
        </el-dialog>

      </div>
    </el-main>
  </el-container>

</template>

<style scoped>
/* 侧边栏样式，宽度200px */
.aside {
  background: #f5f5f5;
  padding: 10px;
  height: 100%;
}

/* 自定义树形菜单样式 */
.custom-tree .el-tree-node {
  background-color: #ffffff;
  border-radius: 5px;
  margin: 5px 0;
  padding: 10px;
  transition: background-color 0.3s ease;
}

.custom-tree .el-tree-node:hover {
  background-color: #f0f0f0;
}

.custom-tree .el-tree-node__content {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.custom-tree .el-tree-node__label {
  color: #409eff;
  font-size: 14px;
}

.custom-tree .el-tree-node__label:hover {
  color: #66b1ff;
}

/* 高亮选中的节点 */
.custom-tree .is-current {
  background-color: #067ff3;
  border-radius: 5px;
}

/* 增加节点间的间距 */
.custom-tree .el-tree-node__expand-icon {
  margin-left: 10px;
}
</style>
