<template>
  <div class="detail-container">
    <div class="header">
      <el-page-header @back="$router.back()">
        <template #content>
          <span class="text-large font-600 mr-3"> 订单详情 </span>
        </template>
        <template #extra>
          <div class="flex items-center">
            <el-button type="primary" class="ml-2" @click="$router.back()">返回列表</el-button>
          </div>
        </template>
      </el-page-header>
    </div>

    <div class="content" v-loading="loading">
      <el-card class="box-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
            <div class="header-actions">
              <el-button type="primary" size="small" @click="openEdit">编辑订单</el-button>
              <el-button
                type="warning"
                size="small"
                @click="handleAfterSales"
                v-if="order.productionProgress === 'FINISHED' && order.installProgress === 'FINISHED'"
              >申请售后</el-button>
              <el-tag :type="order.status === 'DRAFT' ? 'info' : 'success'" class="status-tag">
                {{ order.status === 'DRAFT' ? '草稿' : '已提交' }}
              </el-tag>
              <el-tag v-if="order.status !== 'DRAFT'" :type="getProgressType(order.installProgress)" effect="dark" class="status-tag">
                {{ getProgressLabel(order.installProgress) }}
              </el-tag>
            </div>
          </div>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="客户姓名">{{ order.customerName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ order.customerPhone }}</el-descriptions-item>
          <el-descriptions-item label="安装地址" :span="3">{{ order.address }}</el-descriptions-item>
          <el-descriptions-item label="订单价格">
              <span class="price-text">¥ {{ order.price }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ order.createTime ? order.createTime.replace('T', ' ') : '' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="box-card mt-4" shadow="never">
        <template #header>
          <div class="card-header">
            <span>窗户规格</span>
          </div>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="品牌">{{ order.brand }}</el-descriptions-item>
          <el-descriptions-item label="窗型">{{ order.windowType || '-' }}</el-descriptions-item>
          <el-descriptions-item label="颜色">{{ order.color || '-' }}</el-descriptions-item>
          <el-descriptions-item label="玻璃规格">{{ order.glassSpec || '-' }}</el-descriptions-item>
          <el-descriptions-item label="宽度">{{ order.width }} mm</el-descriptions-item>
          <el-descriptions-item label="高度">{{ order.height }} mm</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="box-card mt-4" shadow="never">
        <template #header>
          <div class="card-header">
            <span>人员信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="销售员">
            <div class="user-info-cell">
              <span class="user-name">{{ order.salespersonName }}</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="安装师傅">
            <div class="user-info-cell">
              <span v-if="order.installerName" class="user-name">{{ order.installerName }}</span>
              <span v-else class="text-gray">未分配</span>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="box-card mt-4" shadow="never">
        <template #header>
          <div class="card-header">
            <span>订单进度</span>
          </div>
        </template>
        <div class="progress-wrapper">
          <div class="progress-label">制作进度</div>
          <el-steps :active="getProductionStep(order.productionProgress)" finish-status="success" align-center>
            <el-step title="等待" :icon="Timer" />
            <el-step title="制作" :icon="Tools" />
            <el-step title="完成" :icon="CircleCheck" />
          </el-steps>
        </div>
        <div class="progress-wrapper">
          <div class="progress-label">安装进度</div>
          <el-steps :active="getInstallStep(order.installProgress)" finish-status="success" align-center>
            <el-step title="等待" :icon="Timer" />
            <el-step title="排期" :icon="Calendar" />
            <el-step title="安装" :icon="Tools" />
            <el-step title="完成" :icon="CircleCheck" />
          </el-steps>
        </div>
        <el-descriptions :column="2" border class="mt-4">
          <el-descriptions-item label="预约安装日期" :span="2" label-class-name="highlight-label-blue" class-name="highlight-content-blue">
            {{ order.scheduledInstallDate ? order.scheduledInstallDate.replace('T', ' ') : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="实际安装完成日期" :span="2" label-class-name="highlight-label-green" class-name="highlight-content-green">
            {{ order.actualInstallEndDate ? order.actualInstallEndDate.replace('T', ' ') : '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="box-card mt-4" shadow="never" v-if="order.status !== 'DRAFT'">
        <template #header>
          <div class="card-header">
            <span>财务信息</span>
            <div>
                 <el-tag :type="getPaymentStatusType(order.paymentStatus)" class="mr-2">{{ getPaymentStatusLabel(order.paymentStatus) }}</el-tag>
                 <el-button type="primary" size="small" @click="handlePayment">添加收款</el-button>
            </div>
          </div>
        </template>
        <el-descriptions :column="3" border>
             <el-descriptions-item label="订单总额">
                 <span class="price-text">¥ {{ order.price || 0 }}</span>
             </el-descriptions-item>
             <el-descriptions-item label="已付金额">
                 <span class="price-text" style="color: #67c23a">¥ {{ order.paidAmount || 0 }}</span>
             </el-descriptions-item>
             <el-descriptions-item label="待付金额">
                 <span class="price-text" style="color: #e6a23c">¥ {{ ((order.price || 0) - (order.paidAmount || 0)).toFixed(2) }}</span>
             </el-descriptions-item>
        </el-descriptions>
        
        <div class="mt-4 edge-to-edge">
            <div class="sub-title">收款记录</div>
            <el-table :data="paymentList" border stripe size="small" class="receipt-table">
                <el-table-column prop="payTime" label="时间" width="180" header-align="center" label-class-name="text-center" />
                <el-table-column prop="amount" label="金额" width="140" align="right" header-align="center" label-class-name="text-center">
                    <template #default="scope"><span class="amount">¥ {{ scope.row.amount }}</span></template>
                </el-table-column>
                <el-table-column prop="payMethod" label="支付方式" width="140" header-align="center" label-class-name="text-center">
                  <template #default="scope">
                    <el-tag :type="getPayMethodTagType(scope.row.payMethod)" class="method-tag">{{ scope.row.payMethod }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createByName" label="添加人" width="120" header-align="center" label-class-name="text-center" />
                <el-table-column label="操作" width="160" fixed="right" header-align="center" align="center" label-class-name="text-center">
                  <template #default="scope">
                    <el-button type="primary" link @click="openPaymentDetail(scope.row)">详情</el-button>
                    <el-divider direction="vertical" />
                    <el-button type="primary" link @click="openPaymentPage(scope.row)">页面查看</el-button>
                  </template>
                </el-table-column>
            </el-table>
        </div>
      </el-card>

    </div>

    <!-- Payment Dialog -->
    <el-dialog v-model="paymentDialogVisible" title="添加收款" width="400px">
        <el-form :model="paymentForm" label-width="80px">
            <el-form-item label="收款金额">
                <el-input-number v-model="paymentForm.amount" :min="0" :precision="2" :step="1" style="width: 100%" />
            </el-form-item>
            <el-form-item label="支付方式">
                <el-select v-model="paymentForm.payMethod" style="width: 100%">
                    <el-option label="微信" value="微信" />
                    <el-option label="支付宝" value="支付宝" />
                    <el-option label="现金" value="现金" />
                    <el-option label="银行转账" value="银行转账" />
                </el-select>
            </el-form-item>
            <el-form-item label="付款凭证">
                <el-upload
                  action="/api/file/upload"
                  :headers="uploadHeaders"
                  :file-list="uploadFileList"
                  :on-success="handleUploadSuccess"
                  :on-remove="handleUploadRemove"
                  multiple
                  :limit="10"
                  accept=".png,.jpg,.jpeg,.gif,.pdf,.doc,.docx,.xls,.xlsx"
                  list-type="picture-card"
                >
                  <el-icon><Plus /></el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model="paymentForm.remark" type="textarea" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="paymentDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitPayment">确定</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog v-model="paymentDetailVisible" title="收款详情" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="时间" :span="2">{{ selectedPayment?.payTime }}</el-descriptions-item>
        <el-descriptions-item label="金额">
          <span class="amount">¥ {{ selectedPayment?.amount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          <el-tag :type="getPayMethodTagType(selectedPayment?.payMethod)">{{ selectedPayment?.payMethod }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ selectedPayment?.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="添加人">{{ selectedPayment?.createByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="附件" :span="2">
          <div v-if="getAttachments(selectedPayment).length > 0" class="attachment-grid scrollable">
            <a v-for="url in getAttachments(selectedPayment)" :key="url" :href="url" target="_blank" class="attachment-card">
              <el-icon><Link /></el-icon>
              <span class="file-name">{{ fileNameFromUrl(url) }}</span>
            </a>
          </div>
          <span v-else class="text-gray">-</span>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="paymentDetailVisible = false">关闭</el-button>
        <el-button type="primary" @click="openPaymentPage(selectedPayment)">在新页面查看</el-button>
      </template>
    </el-dialog>

    <!-- After Sales Dialog -->
    <el-dialog v-model="afterSalesDialogVisible" title="申请售后" width="500px">
        <el-form :model="afterSalesForm" label-width="80px">
            <el-form-item label="问题描述">
                <el-input v-model="afterSalesForm.issueDescription" type="textarea" :rows="3" placeholder="请详细描述问题" />
            </el-form-item>
            <el-form-item label="期望时间">
                <el-date-picker v-model="afterSalesForm.appointmentTime" type="datetime" style="width: 100%" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm:ss" placeholder="期望上门时间" />
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="afterSalesDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitAfterSales">提交</el-button>
        </template>
    </el-dialog>

    <!-- Edit Dialog -->
    <el-dialog v-model="editDialogVisible" title="编辑订单" width="600px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="订单状态">
          <el-radio-group v-model="editForm.status">
            <el-radio label="DRAFT">草稿</el-radio>
            <el-radio label="SUBMITTED">正式提交</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="制作进度">
          <el-select v-model="editForm.productionProgress" style="width: 100%" :disabled="editForm.status === 'DRAFT'">
            <el-option label="等待中" value="WAITING" />
            <el-option label="制作中" value="PRODUCING" />
            <el-option label="已完成" value="FINISHED" />
          </el-select>
        </el-form-item>
        <el-form-item label="安装进度">
          <el-select v-model="editForm.installProgress" style="width: 100%" :disabled="editForm.status === 'DRAFT'">
            <el-option label="等待中" value="WAITING" />
            <el-option label="已排期" value="SCHEDULED" />
            <el-option label="安装中" value="INSTALLING" :disabled="editForm.productionProgress !== 'FINISHED'" />
            <el-option label="已完成" value="FINISHED" :disabled="editForm.productionProgress !== 'FINISHED'" />
          </el-select>
        </el-form-item>
        <el-form-item label="安装师傅">
          <el-select v-model="editForm.installerId" style="width: 100%" filterable>
            <el-option v-for="item in installerList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约安装">
          <el-date-picker v-model="editForm.scheduledInstallDate" type="datetime" style="width: 100%" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="实际完成">
          <el-date-picker v-model="editForm.actualInstallEndDate" type="datetime" style="width: 100%" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { createPayment, listPayments } from '@/api/payment'
import { createAfterSales } from '@/api/afterSales'
import { updateOrder } from '@/api/order'
import { ElMessage } from 'element-plus'
import { Timer, Tools, CircleCheck, Calendar, Van, Money, Wallet, Plus, Link } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const order = ref({})
const paymentList = ref([])
const paymentDialogVisible = ref(false)
const paymentDetailVisible = ref(false)
const selectedPayment = ref(null)
const afterSalesDialogVisible = ref(false)
const editDialogVisible = ref(false)
const paymentForm = ref({
    amount: 0,
    payMethod: '微信',
    remark: '',
    attachments: []
})
const uploadHeaders = ref({
  Authorization: `Bearer ${localStorage.getItem('token') || ''}`
})
const uploadFileList = ref([])
const editForm = ref({
  id: null,
  status: 'SUBMITTED',
  productionProgress: 'WAITING',
  installProgress: 'WAITING',
  installerId: null,
  scheduledInstallDate: null,
  actualInstallEndDate: null
})
const afterSalesForm = ref({
    issueDescription: '',
    appointmentTime: null
})

onMounted(() => {
    const id = route.params.id
    if (id) {
        fetchDetail(id)
        fetchPayments(id)
    }
})

const fetchDetail = async (id) => {
    loading.value = true
    try {
        const res = await request.get(`/order/detail/${id}`)
        if (res.code === 200) {
            order.value = res.data
        } else {
            ElMessage.error(res.message)
        }
    } catch (e) {
        // handled
    } finally {
        loading.value = false
    }
}

const fetchPayments = async (id) => {
    try {
        const res = await listPayments(id)
        if (res.code === 200) {
            paymentList.value = res.data
        }
    } catch(e) {}
}

const handlePayment = () => {
    paymentForm.value = { amount: 0, payMethod: '微信', remark: '', attachments: [] }
    uploadFileList.value = []
    paymentDialogVisible.value = true
}

const handleAfterSales = () => {
    afterSalesForm.value = { issueDescription: '', appointmentTime: null }
    afterSalesDialogVisible.value = true
}

const openEdit = () => {
  const cu = userStore.currentUser
  let canEdit = false
  if (cu.role === 'ADMIN') canEdit = true
  else if (cu.role === 'SALES' && order.value.salespersonId === cu.id) canEdit = true
  if (!canEdit) {
    ElMessage.warning('无权编辑此订单')
    return
  }
  editForm.value = {
    id: order.value.id,
    status: order.value.status || 'SUBMITTED',
    productionProgress: order.value.productionProgress || 'WAITING',
    installProgress: order.value.installProgress || 'WAITING',
    installerId: order.value.installerId || null,
    scheduledInstallDate: order.value.scheduledInstallDate || null,
    actualInstallEndDate: order.value.actualInstallEndDate || null
  }
  editDialogVisible.value = true
}

const submitPayment = async () => {
    if (paymentForm.value.amount <= 0) {
        ElMessage.warning('金额必须大于0')
        return
    }
    const price = Number(order.value.price || 0)
    const paid = Number(order.value.paidAmount || 0)
    const add = Number(paymentForm.value.amount || 0)
    if (price > 0 && paid + add > price) {
        ElMessage.warning('收款总额不能超过订单总额')
        return
    }
    try {
        const res = await createPayment({
            orderId: order.value.id,
            amount: paymentForm.value.amount,
            payMethod: paymentForm.value.payMethod,
            remark: paymentForm.value.remark || '',
            attachments: paymentForm.value.attachments || []
        })
        if (res.code === 200) {
            ElMessage.success('收款成功')
            paymentDialogVisible.value = false
            fetchDetail(order.value.id) // refresh order status
            fetchPayments(order.value.id)
        } else {
            ElMessage.error(res.message)
        }
    } catch(e) {}
}

const openPaymentDetail = (row) => {
  selectedPayment.value = row
  paymentDetailVisible.value = true
}

const openPaymentPage = (row) => {
  if (!row || !order.value?.id) return
  router.push({ name: 'PaymentDetail', params: { id: row.id }, query: { orderId: order.value.id } })
}

const handleUploadSuccess = (response, file, fileList) => {
  if (response && response.code === 200) {
    const url = response.data
    paymentForm.value.attachments.push(url)
    uploadFileList.value = fileList.map(f => ({
      name: f.name,
      url: f.response?.data || f.url
    }))
  } else {
    ElMessage.error('上传失败')
  }
}

const handleUploadRemove = (file, fileList) => {
  const url = file.url || (file.response && file.response.data)
  if (url) {
    paymentForm.value.attachments = paymentForm.value.attachments.filter(u => u !== url)
  }
  uploadFileList.value = fileList.map(f => ({
    name: f.name,
    url: f.response?.data || f.url
  }))
}

const parseAttachments = (value) => {
  if (!value) return []
  return value.split(',').map(s => s.trim()).filter(Boolean)
}

const getAttachments = (row) => {
  if (!row) return []
  if (Array.isArray(row.attachmentList)) return row.attachmentList
  if (typeof row.attachments === 'string') return parseAttachments(row.attachments)
  return []
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

const submitAfterSales = async () => {
    if (!afterSalesForm.value.issueDescription) {
        ElMessage.warning('请填写问题描述')
        return
    }
    try {
        const res = await createAfterSales({
            orderId: order.value.id,
            customerName: order.value.customerName,
            customerPhone: order.value.customerPhone,
            address: order.value.address,
            ...afterSalesForm.value
        })
        if (res.code === 200) {
            ElMessage.success('售后工单已提交')
            afterSalesDialogVisible.value = false
        } else {
            ElMessage.error(res.message)
        }
    } catch(e) {}
}

const submitEdit = async () => {
  const cu = userStore.currentUser
  const payload = {
    ...editForm.value,
    currentUserId: cu.id,
    currentUserRole: cu.role
  }
  if (payload.status === 'DRAFT') {
    payload.installProgress = 'WAITING'
    payload.productionProgress = 'WAITING'
  }
  try {
    const res = await updateOrder(payload)
    if (res.code === 200) {
      ElMessage.success('更新成功')
      editDialogVisible.value = false
      fetchDetail(order.value.id)
    } else {
      ElMessage.error(res.message)
    }
  } catch(e) {}
}

const getPaymentStatusType = (status) => {
  const map = {
    'UNPAID': 'danger',
    'PARTIAL': 'warning',
    'PAID': 'success'
  }
  return map[status] || 'info'
}

const getPaymentStatusLabel = (status) => {
  const map = {
    'UNPAID': '未支付',
    'PARTIAL': '部分支付',
    'PAID': '已付清'
  }
  return map[status] || '未支付'
}

const getPayMethodTagType = (method) => {
  const map = {
    '微信': 'success',
    '支付宝': 'primary',
    '现金': 'warning',
    '银行转账': 'info'
  }
  return map[method] || 'info'
}

const getProgressType = (status) => {
  const map = {
    'WAITING': 'info',
    'SCHEDULED': 'warning',
    'INSTALLING': 'primary',
    'PRODUCING': 'primary',
    'FINISHED': 'success'
  }
  return map[status] || 'info'
}

const getProgressLabel = (status) => {
  const map = {
    'WAITING': '等待中',
    'SCHEDULED': '已排期',
    'INSTALLING': '安装中',
    'PRODUCING': '制作中',
    'FINISHED': '已完成'
  }
  return map[status] || status
}

const getInstallStep = (status) => {
    const map = {
        'WAITING': 0,
        'SCHEDULED': 1,
        'INSTALLING': 2,
        'FINISHED': 3
    }
    if (status === 'FINISHED') return 4
    return map[status] || 0
}

const getProductionStep = (status) => {
    const map = {
        'WAITING': 0,
        'PRODUCING': 1,
        'FINISHED': 2
    }
    if (status === 'FINISHED') return 3
    return map[status] || 0
}
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

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.price-text {
    color: #f56c6c;
    font-weight: bold;
    font-size: 16px;
}

.text-gray {
    color: #909399;
}

.mt-4 {
    margin-top: 16px;
}

.user-info-cell {
    margin-bottom: 12px;
    font-weight: 500;
    font-size: 15px;
}

.progress-wrapper {
    background-color: #f9fafc;
    padding: 20px;
    border-radius: 8px;
    margin-top: 10px;
}

.progress-label {
    font-size: 14px;
    font-weight: 600;
    color: #606266;
    margin-bottom: 15px;
    border-left: 3px solid #409EFF;
    padding-left: 8px;
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
}

.attachment-list {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.attachment-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #409EFF;
  text-decoration: none;
}

.attachment-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 8px;
}
.attachment-card {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 10px;
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
.scrollable {
  max-height: 300px;
  overflow-y: auto;
  padding-right: 4px;
}

.edge-to-edge {
  margin-left: -20px;
  margin-right: -20px;
}

:deep(.receipt-table) {
  border-radius: 8px;
  overflow: hidden;
}
:deep(.receipt-table .el-table__header-wrapper th) {
  background-color: #f7f9fc;
  color: #606266;
  font-weight: 600;
}
:deep(.receipt-table .el-table__header-wrapper th .cell) { text-align: center; }
:deep(.receipt-table .el-table__header-wrapper th.text-left .cell) { text-align: left; }
:deep(.receipt-table .el-table__header-wrapper th.text-right .cell) { text-align: right; }
:deep(.receipt-table .el-table__header-wrapper th.text-center .cell) { text-align: center; }
:deep(.receipt-table .el-table__body .cell) {
  padding: 8px 12px;
}
:deep(.receipt-table.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fcfdff;
}
:deep(.receipt-table .el-table__body tr:hover td) {
  background-color: #f9fbff;
}
.sub-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
  padding-left: 10px;
  border-left: 4px solid #409EFF;
}
.amount {
  color: #67c23a;
  font-weight: 600;
  font-variant-numeric: tabular-nums;
}
.method-tag {
  border-radius: 12px;
}
:deep(.highlight-label-blue) {
    background-color: #ecf5ff !important;
    color: #409eff;
    font-weight: bold;
}
:deep(.highlight-content-blue) {
    background-color: #f4f9ff !important;
}
:deep(.highlight-label-green) {
    background-color: #f0f9eb !important;
    color: #67c23a;
    font-weight: bold;
}
:deep(.highlight-content-green) {
    background-color: #f5fbf0 !important;
}
</style>
