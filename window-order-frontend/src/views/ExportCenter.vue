<template>
  <div class="page-container">
    <div class="header">
      <h2>导出中心</h2>
    </div>

    <el-table :data="tasks" v-loading="loading" border style="width: 100%">
      <el-table-column prop="taskName" label="任务名称" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createByName" label="创建人" width="150" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="finishTime" label="完成时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button 
            v-if="scope.row.status === 'COMPLETED' && scope.row.fileUrl" 
            type="primary" 
            link 
            @click="downloadFile(scope.row.fileUrl)"
          >
            下载
          </el-button>
          <span v-else-if="scope.row.status === 'FAILED'" class="text-danger">{{ scope.row.errorMsg }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { listTasks } from '@/api/exportTask'
import { ElMessage } from 'element-plus'

const tasks = ref([])
const loading = ref(false)
let timer = null

const fetchTasks = async () => {
  try {
    const res = await listTasks()
    if (res && res.code === 200) {
        tasks.value = res.data
    }
  } catch (error) {
    console.error(error)
  }
}

const downloadFile = (url) => {
  window.open(url, '_blank')
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'info',
    'PROCESSING': 'warning',
    'COMPLETED': 'success',
    'FAILED': 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '等待中',
    'PROCESSING': '处理中',
    'COMPLETED': '完成',
    'FAILED': '失败'
  }
  return map[status] || status
}

onMounted(() => {
  fetchTasks()
  // Auto refresh every 5 seconds
  timer = setInterval(fetchTasks, 5000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.text-danger {
  color: #f56c6c;
}
</style>
