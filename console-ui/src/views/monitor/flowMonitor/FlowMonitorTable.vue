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
const emit = defineEmits(['pageChange']);

</script>

<template>
  <el-table v-loading="loading" :data="dataRows" size="large" header-cell-class-name="table-header">
    <el-table-column prop="flowName" label="流程名称" width="300" />
    <el-table-column prop="flowKey" label="流程编码" width="200" />
    <el-table-column prop="flowVersion" label="版本" width="100" />
    <el-table-column label="状态" width="150" >
      <template #default="scope">
        <el-tag v-if="scope.row.status === 'Started'" class="status-tag" type="success" >Started</el-tag>
        <el-tag v-else-if="scope.row.status === 'Stopped'" class="status-tag" type="danger" >Stopped</el-tag>
        <el-tag v-else class="status-tag" type="info" >{{ scope.row.status }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="completed" label="completed" width="120" />
    <el-table-column prop="failed" label="failed" width="100" />
    <el-table-column prop="total" label="total" width="100" />
    <el-table-column prop="handled" label="handled" width="100" />
    <el-table-column prop="inFlight" label="inFlight" width="100" />
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

<style scoped>
.status-tag {
  font-size: 14px;
  border-radius: 16px; /* 椭圆形 */
  padding: 5px 12px;
}
</style>
