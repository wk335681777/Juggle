<script lang="ts" setup>
import { useRouter } from 'vue-router';
const router = useRouter();

defineProps({
  dataRows: {
    type: Array,
    default: [],
  },
  pageNum: Number,
  pageSize: Number,
  dataTotal: Number,
  loading: Boolean,
});
const emit = defineEmits(['pageChange', 'flowVersionStatusChange', 'delete','restore']);

function deleteRow(row: any, index: number) {
  emit('delete', row, index);
}

function updateFlowVersionStatus(row: any) {
  emit('flowVersionStatusChange', row);
}

function flowVersionStatusOptFormat(flowVersionStatus: number) {
  if (flowVersionStatus == 0) {
    return '启用';
  } else {
    return '禁用';
  }
}

function buildFullTriggerUrl(triggerUrl: string) {
  const origin = window.location.origin;
  return origin + triggerUrl;
}

function goDesignViewPage(flowVersionId: number, flowKey: string) {
  const path = "/design/view/"+flowVersionId+"/"+flowKey;
  const { href } = router.resolve({ path })
  window.open(href, '_blank')
}

function versionRestoreRow(row: any) {
  emit('restore', row);
}

</script>

<template>
  <el-table v-loading="loading" :data="dataRows" size="large" header-cell-class-name="table-header">
    <el-table-column prop="flowName" label="流程名称" width="200" />
    <el-table-column prop="flowVersion" label="版本" width="150" />
    <el-table-column prop="flowVersion" label="流程状态" width="150">
      <template #default="scope">
        <el-tag v-if="scope.row.flowVersionStatus == 1" type="success">启用</el-tag>
        <el-tag v-else type="danger">禁用</el-tag>
      </template>
    </el-table-column>
<!--    <el-table-column prop="triggerUrl" label="访问地址" width="480">-->
<!--      <template #default="scope">-->
<!--        {{ buildFullTriggerUrl(scope.row.triggerUrl) }}-->
<!--      </template>-->
<!--    </el-table-column>-->
    <el-table-column prop="flowVersionRemark" label="版本说明" width="520" show-overflow-tooltip />
    <el-table-column label="操作" width="250">
      <template #default="scope">
        <el-button link type="primary" size="small" @click.prevent="updateFlowVersionStatus(scope.row)">
          {{ flowVersionStatusOptFormat(scope.row.flowVersionStatus) }}
        </el-button>
        <el-button link type="primary" size="small" @click.prevent="deleteRow(scope.row, scope.$index)"> 删除 </el-button>
        <el-button link type="primary" size="small" @click.prevent="goDesignViewPage(scope.row.id, scope.row.flowKey)"> 查看 </el-button>
        <el-button link type="primary" size="small" @click.prevent="versionRestoreRow(scope.row)"> 从此版本还原 </el-button>
      </template>
    </el-table-column>
  </el-table>
  <div class="table-pagination">
    <el-pagination
      :currentPage="pageNum"
      :pageSize="pageSize"
      background
      layout="total, prev, pager, next"
      :total="dataTotal"
      @currentChange="(val: number) => $emit('pageChange', val)"
    />
  </div>
</template>

<style lang="less" scoped>

</style>
