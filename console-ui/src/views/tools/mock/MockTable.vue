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
const emit = defineEmits(['pageChange', 'deploy', 'edit', 'delete']);

function deleteRow(row: any, index: number) {
  emit('delete', row, index);
}

function editRow(row: any) {
  emit('edit', row);
}

function viewRow(row: any) {
  emit('view', row);
}
</script>

<template>
  <el-table v-loading="loading" :data="dataRows" size="large" header-cell-class-name="table-header">
    <el-table-column prop="name" label="名称" width="180" />
    <el-table-column prop="url" label="url" width="400" />
    <el-table-column prop="remark" label="描述" width="320" show-overflow-tooltip />
    <el-table-column prop="updatedAt" label="修改时间" width="200" />
    <el-table-column label="操作" width="200">
      <template #default="scope">
        <el-button link type="primary" size="small" @click.prevent="viewRow(scope.row)">查看</el-button>
        <el-button link type="primary" size="small" @click.prevent="editRow(scope.row)"> 编辑 </el-button>
        <el-button link type="primary" size="small" @click.prevent="deleteRow(scope.row, scope.$index)"> 删除 </el-button>
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
