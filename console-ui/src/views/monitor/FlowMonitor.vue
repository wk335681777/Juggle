<script setup lang="ts">
import { flowMonitorService } from '@/service';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { FlowMonitorTable, FlowMonitorFilter } from './flowMonitor'

const router = useRouter();

const props = defineProps({
  appCode: {
    type: String,
    required: true
  }
});

const pageNum = ref(1);
const pageSize = ref(200);
const dataTotal = ref(0);
const dataRows = ref<Record<string, any>[]>([]);
const loading = ref(false);

const pageNumProd = ref(1);
const pageSizeProd = ref(200);
const dataTotalProd = ref(0);
const dataRowsProd = ref<Record<string, any>[]>([]);
const loadingProd = ref(false);

const workerIpList = ref([]);
const selectedIp = ref('');
const filter = ref<{
  flowName?: string;
  flowType?: string;
}>({});

// 初始加载

queryServerList();

function onSearch(param: typeof filter.value) {
  filter.value = param;
  onDevPageChange(1);
  onProdPageChange(1);
}

function onDevPageChange(page: number) {
  pageNum.value = page;
  devQueryPage();
}

function onProdPageChange(page: number) {
  pageNumProd.value = page;
  prodQueryPage();
}

async function queryServerList() {
  const res = await flowMonitorService.queryServerIpList();
  if (res.success) {
    workerIpList.value = res.result;
    if (workerIpList.value.length > 0) {
      selectedIp.value = workerIpList.value[0];
    }

     devQueryPage();
     prodQueryPage();

  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
}

async function devQueryPage() {
  loading.value = true;
  const res = await flowMonitorService.pageQuery({
    pageSize: pageSize.value,
    pageNum: pageNum.value,
    appCode: props.appCode,
    contextType: 'dev-camel-context',
    ip: selectedIp.value,
    ...filter.value,
  });
  if (res.success) {
    dataTotal.value = res.total;
    dataRows.value = res.result;
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
  loading.value = false;
}

async function prodQueryPage() {
  loadingProd.value = true;
  const res = await flowMonitorService.pageQuery({
    pageSize: pageSizeProd.value,
    pageNum: pageNumProd.value,
    appCode: props.appCode,
    contextType: 'prod-camel-context',
    ip: selectedIp.value,
    ...filter.value,
  });
  if (res.success) {
    dataTotalProd.value = res.total;
    dataRowsProd.value = res.result;
  } else {
    ElMessage({ type: 'error', message: res.errorMsg });
  }
  loadingProd.value = false;
}

async function requestMonitorData(ip) {
  selectedIp.value = ip;
   devQueryPage()
   prodQueryPage()

}
</script>

<template>
  <div class="page-flow-define">
    <el-container>
      <el-header class="page-header">
        <FlowMonitorFilter @search="onSearch" />
        <el-button v-for="(ip, index) in workerIpList" :key="index" @click="requestMonitorData(ip)" :class="{'selected': selectedIp === ip}">
          {{ ip }}
        </el-button>
      </el-header>
      <el-main class="page-body">
        <el-tabs model-value="prod">
          <el-tab-pane label="线上环境" name="prod">
            <FlowMonitorTable
                :dataRows="dataRowsProd"
                :dataTotal="dataTotalProd"
                :pageNum="pageNumProd"
                :pageSize="pageSizeProd"
                :loading="loadingProd"
                @pageChange="onProdPageChange"
            />
          </el-tab-pane>
          <el-tab-pane label="调试环境" name="dev">
            <FlowMonitorTable
                :dataRows="dataRows"
                :dataTotal="dataTotal"
                :pageNum="pageNum"
                :pageSize="pageSize"
                :loading="loading"
                @pageChange="onDevPageChange"
            />
          </el-tab-pane>
        </el-tabs>

      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
/* 选中按钮的高亮效果 */
.selected {
  background-color: #409eff !important;
  color: white !important;
  border-color: #409eff !important;
}
</style>