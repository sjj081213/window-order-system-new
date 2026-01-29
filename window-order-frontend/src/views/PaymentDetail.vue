<template>
  <div class="detail-container" v-loading="loading">
    <div class="header">
      <el-page-header @back="goBack">
        <template #content>
          <span class="text-large font-600 mr-3"> 收款详情 </span>
        </template>
      </el-page-header>
    </div>
    <div class="content">
      <el-card class="box-card" shadow="never">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="时间">{{ payment?.payTime }}</el-descriptions-item>
          <el-descriptions-item label="添加人">{{ payment?.createByName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="金额">
            <span class="amount">¥ {{ payment?.amount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="支付方式">
            <el-tag :type="getPayMethodTagType(payment?.payMethod)">{{ payment?.payMethod }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ payment?.remark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="附件" :span="2">
            <div v-if="attachments.length" class="attachment-grid">
              <div v-for="url in attachments" :key="url" class="attachment-item">
                <el-image 
                  v-if="isImage(url)"
                  :src="url" 
                  :preview-src-list="[url]"
                  fit="cover"
                  class="attachment-image"
                />
                <a v-else :href="url" target="_blank" class="attachment-card">
                  <el-icon><Link /></el-icon>
                  <span class="file-name">{{ fileNameFromUrl(url) }}</span>
                </a>
              </div>
            </div>
            <span v-else class="text-gray">-</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { listPayments } from '@/api/payment'
import { ElMessage } from 'element-plus'
import { Link } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const payment = ref(null)
const attachments = computed(() => {
  if (!payment.value) return []
  if (Array.isArray(payment.value.attachmentList)) return payment.value.attachmentList
  if (typeof payment.value.attachments === 'string') {
    return payment.value.attachments.split(',').map(s => s.trim()).filter(Boolean)
  }
  return []
})

const getPayMethodTagType = (method) => {
  const map = {
    '微信': 'success',
    '支付宝': 'primary',
    '现金': 'warning',
    '银行转账': 'info'
  }
  return map[method] || 'info'
}

const isImage = (url) => {
  const ext = url.split('.').pop().toLowerCase()
  return ['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)
}

const fileNameFromUrl = (url) => {
  try {
    const u = new URL(url, window.location.origin)
    const pathname = u.pathname || url
    const name = pathname.split('/').pop() || url
    return decodeURIComponent(name)
  } catch {
    const parts = url.split('?')[0].split('/')
    return decodeURIComponent(parts.pop() || url)
  }
}

const goBack = () => {
  const orderId = route.query.orderId
  if (orderId) {
    router.push({ name: 'OrderDetail', params: { id: orderId } })
  } else {
    router.back()
  }
}

onMounted(async () => {
  const id = Number(route.params.id)
  const orderId = Number(route.query.orderId)
  if (!orderId) {
    ElMessage.warning('缺少订单编号，无法加载收款详情')
    return
  }
  loading.value = true
  try {
    const res = await listPayments(orderId)
    if (res.code === 200 && Array.isArray(res.data)) {
      payment.value = res.data.find(p => p.id === id) || null
      if (!payment.value) {
        ElMessage.warning('未找到该收款记录')
      }
    }
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.detail-container {
  background-color: transparent;
  min-height: 100%;
  display: flex;
  flex-direction: column;
}
.header {
  background-color: #fff;
  padding: 16px 24px;
  margin: 0;
  width: 100%;
  box-sizing: border-box;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.content {
  max-width: 100%;
  margin: 24px auto;
  width: 100%;
  box-sizing: border-box;
  padding: 0;
}
.box-card {
  margin-bottom: 20px;
}
.amount {
  color: #67c23a;
  font-weight: 600;
  font-variant-numeric: tabular-nums;
}
.attachment-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 10px;
}
.attachment-card {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  color: #409EFF;
  text-decoration: none;
  background: #fff;
}
.file-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}
.attachment-image {
  width: 100%;
  height: 160px;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}
.text-gray {
  color: #909399;
}
</style>

