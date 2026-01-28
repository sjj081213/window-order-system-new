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
            <div>
                 <el-button type="warning" size="small" @click="handleAfterSales" v-if="order.status !== 'DRAFT'">申请售后</el-button>
                 <el-tag :type="getProgressType(order.installProgress)" effect="dark" class="status-tag ml-2">
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
            <span>进度与人员</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="销售员">
             <div class="user-info-cell">
                 <span class="user-name">{{ order.salespersonName }}</span>
             </div>
             <div class="progress-wrapper">
                 <div class="progress-label">生产进度</div>
                 <el-steps :active="getProductionStep(order.productionProgress)" finish-status="success" align-center>
                    <el-step title="等待" :icon="Timer" />
                    <el-step title="生产" :icon="Tools" />
                    <el-step title="完成" :icon="CircleCheck" />
                 </el-steps>
             </div>
          </el-descriptions-item>
          <el-descriptions-item label="安装师傅">
              <div class="user-info-cell">
                  <span v-if="order.installerName" class="user-name">{{ order.installerName }}</span>
                  <span v-else class="text-gray">未分配</span>
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
          </el-descriptions-item>
          <el-descriptions-item label="预约安装日期" :span="2" label-class-name="highlight-label-blue" class-name="highlight-content-blue">
            {{ order.scheduledInstallDate ? order.scheduledInstallDate.replace('T', ' ') : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="实际安装完成日期" :span="2" label-class-name="highlight-label-green" class-name="highlight-content-green">
            {{ order.actualInstallEndDate ? order.actualInstallEndDate.replace('T', ' ') : '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="box-card mt-4" shadow="never">
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
        
        <div class="mt-4">
            <div class="sub-title">收款记录</div>
            <el-table :data="paymentList" border stripe size="small">
                <el-table-column prop="payTime" label="时间" width="160" />
                <el-table-column prop="amount" label="金额" width="120">
                    <template #default="scope">¥ {{ scope.row.amount }}</template>
                </el-table-column>
                <el-table-column prop="payMethod" label="支付方式" width="120" />
                <el-table-column prop="remark" label="备注" />
                <el-table-column prop="createByName" label="操作人" width="100" />
            </el-table>
        </div>
      </el-card>

    </div>

    <!-- Payment Dialog -->
    <el-dialog v-model="paymentDialogVisible" title="添加收款" width="400px">
        <el-form :model="paymentForm" label-width="80px">
            <el-form-item label="收款金额">
                <el-input-number v-model="paymentForm.amount" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
            <el-form-item label="支付方式">
                <el-select v-model="paymentForm.payMethod" style="width: 100%">
                    <el-option label="微信" value="微信" />
                    <el-option label="支付宝" value="支付宝" />
                    <el-option label="现金" value="现金" />
                    <el-option label="银行转账" value="银行转账" />
                </el-select>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { createPayment, listPayments } from '@/api/payment'
import { createAfterSales } from '@/api/afterSales'
import { ElMessage } from 'element-plus'
import { Timer, Tools, CircleCheck, Calendar, Van, Money, Wallet } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref({})
const paymentList = ref([])
const paymentDialogVisible = ref(false)
const afterSalesDialogVisible = ref(false)
const paymentForm = ref({
    amount: 0,
    payMethod: '微信',
    remark: ''
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
    paymentForm.value = { amount: 0, payMethod: '微信', remark: '' }
    paymentDialogVisible.value = true
}

const handleAfterSales = () => {
    afterSalesForm.value = { issueDescription: '', appointmentTime: null }
    afterSalesDialogVisible.value = true
}

const submitPayment = async () => {
    if (paymentForm.value.amount <= 0) {
        ElMessage.warning('金额必须大于0')
        return
    }
    try {
        const res = await createPayment({
            orderId: order.value.id,
            ...paymentForm.value
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
    'PRODUCING': '生产中',
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
