<template>
  <div class="dashboard-page">
    <div class="hero">
      <div class="hero-left">
        <div class="hero-title">
          <el-icon><DataBoard /></el-icon>
          <span>数据驾驶舱</span>
        </div>
        <div class="hero-subtitle">欢迎回来，{{ displayName }} · {{ todayText }}</div>
      </div>
      <div class="hero-actions">
        <el-button type="primary" class="hero-primary" @click="go('/orders')">
          管理订单
          <el-icon class="btn-icon"><ArrowRight /></el-icon>
        </el-button>
        <el-button @click="go('/customers')">客户档案</el-button>
        <el-button v-if="isAdmin" @click="go('/users')">账号管理</el-button>
      </div>
    </div>

    <div v-if="loading" class="loading-skeleton">
      <el-row :gutter="18">
        <el-col v-for="i in 5" :key="i" :xs="24" :sm="12" :md="6">
          <el-skeleton animated>
            <template #template>
              <el-skeleton-item variant="rect" style="height: 128px; border-radius: 16px;" />
            </template>
          </el-skeleton>
        </el-col>
      </el-row>
      <el-row :gutter="18" style="margin-top: 18px;">
        <el-col :xs="24" :lg="14">
          <el-skeleton animated>
            <template #template>
              <el-skeleton-item variant="rect" style="height: 380px; border-radius: 16px;" />
            </template>
          </el-skeleton>
        </el-col>
        <el-col :xs="24" :lg="10" style="margin-top: 18px;">
          <el-skeleton animated>
            <template #template>
              <el-skeleton-item variant="rect" style="height: 380px; border-radius: 16px;" />
            </template>
          </el-skeleton>
        </el-col>
      </el-row>
    </div>

    <template v-else>
      <el-row :gutter="18" class="kpi-row">
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="never" class="kpi-card kpi-pending">
            <div class="kpi-inner">
              <div class="kpi-icon">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="kpi-meta">
                <div class="kpi-title">待处理订单</div>
                <div class="kpi-value">{{ formatNumber(stats.pendingOrders) }}</div>
                <div class="kpi-foot">需要跟进</div>
              </div>
              <el-tag class="kpi-tag" type="warning" effect="dark">Pending</el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="never" class="kpi-card kpi-finished">
            <div class="kpi-inner">
              <div class="kpi-icon">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="kpi-meta">
                <div class="kpi-title">已完成订单</div>
                <div class="kpi-value">{{ formatNumber(stats.finishedOrders) }}</div>
                <div class="kpi-foot">交付成功</div>
              </div>
              <el-tag class="kpi-tag" type="success" effect="dark">Finished</el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="never" class="kpi-card kpi-total">
            <div class="kpi-inner">
              <div class="kpi-icon">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="kpi-meta">
                <div class="kpi-title">总订单数</div>
                <div class="kpi-value">{{ formatNumber(stats.totalOrders) }}</div>
                <div class="kpi-foot">累计沉淀</div>
              </div>
              <el-tag class="kpi-tag" type="info" effect="dark">Total</el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="never" class="kpi-card kpi-money">
            <div class="kpi-inner">
              <div class="kpi-icon">
                <el-icon><Coin /></el-icon>
              </div>
              <div class="kpi-meta">
                <div class="kpi-title">本月销售额</div>
                <div class="kpi-value money">{{ formatMoney(stats.monthlySales) }}</div>
                <div class="kpi-foot">合同金额</div>
              </div>
              <el-tag class="kpi-tag" type="danger" effect="dark">Monthly</el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="never" class="kpi-card kpi-paid">
            <div class="kpi-inner">
              <div class="kpi-icon">
                <el-icon><CreditCard /></el-icon>
              </div>
              <div class="kpi-meta">
                <div class="kpi-title">本月已支付金额</div>
                <div class="kpi-value money">{{ formatMoney(stats.monthlyPaidAmount) }}</div>
                <div class="kpi-foot">实收金额</div>
              </div>
              <el-tag class="kpi-tag" type="success" effect="dark">Paid</el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="18" class="grid-row">
        <el-col :xs="24" :lg="14">
          <el-card shadow="never" class="panel-card">
            <template #header>
              <div class="panel-header">
                <div class="panel-title">
                  <el-icon><DataLine /></el-icon>
                  <span>近7日订单趋势</span>
                </div>
                <div class="panel-hint">订单数变化</div>
              </div>
            </template>
            <div class="chart-container">
              <el-empty v-if="!stats.orderTrend?.length" description="暂无数据" />
              <v-chart v-else class="chart" :option="lineOption" autoresize />
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :lg="10" class="grid-col-right">
          <el-card shadow="never" class="panel-card">
            <template #header>
              <div class="panel-header">
                <div class="panel-title">
                  <el-icon><PieChartIcon /></el-icon>
                  <span>品牌分布</span>
                </div>
                <div class="panel-hint">占比结构</div>
              </div>
            </template>
            <div class="chart-container">
              <el-empty v-if="!stats.brandDistribution?.length" description="暂无数据" />
              <v-chart v-else class="chart" :option="pieOption" autoresize />
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="18" class="grid-row">
        <el-col :xs="24" :lg="16">
          <el-card shadow="never" class="panel-card">
            <template #header>
              <div class="panel-header">
                <div class="panel-title">
                  <el-icon><Trophy /></el-icon>
                  <span>本月销售人员业绩排行</span>
                </div>
                <div class="panel-hint">Top Sales</div>
              </div>
            </template>
            <el-table :data="stats.salesPerformance" size="small" stripe class="rank-table">
              <el-table-column type="index" label="#" width="56" />
              <el-table-column prop="name" label="销售员" min-width="140" />
              <el-table-column prop="orderCount" label="订单数" min-width="100" />
              <el-table-column label="销售额" min-width="160">
                <template #default="scope">
                  <span class="money-text">{{ formatMoney(scope.row.amount) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="进度" min-width="220">
                <template #default="scope">
                  <el-progress
                    :percentage="salesPercent(scope.row.amount)"
                    :stroke-width="8"
                    :show-text="false"
                  />
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :xs="24" :lg="8" class="grid-col-right">
          <el-card shadow="never" class="panel-card quick-card">
            <template #header>
              <div class="panel-header">
                <div class="panel-title">
                  <el-icon><Guide /></el-icon>
                  <span>快捷入口</span>
                </div>
                <div class="panel-hint">Quick Actions</div>
              </div>
            </template>
            <div class="quick-list">
              <div class="quick-item" @click="go('/orders')">
                <div class="quick-icon">
                  <el-icon><List /></el-icon>
                </div>
                <div class="quick-text">
                  <div class="quick-title">订单管理</div>
                  <div class="quick-desc">新增、跟进、查询订单</div>
                </div>
                <el-icon class="quick-arrow"><ArrowRight /></el-icon>
              </div>
              <div class="quick-item" @click="go('/customers')">
                <div class="quick-icon">
                  <el-icon><UserFilled /></el-icon>
                </div>
                <div class="quick-text">
                  <div class="quick-title">客户档案</div>
                  <div class="quick-desc">客户信息与历史订单</div>
                </div>
                <el-icon class="quick-arrow"><ArrowRight /></el-icon>
              </div>
              <div
                v-if="isInstallerOrAdmin"
                class="quick-item"
                @click="go('/remeasure-tasks')"
              >
                <div class="quick-icon">
                  <el-icon><Tools /></el-icon>
                </div>
                <div class="quick-text">
                  <div class="quick-title">复尺任务</div>
                  <div class="quick-desc">任务列表与进度跟踪</div>
                </div>
                <el-icon class="quick-arrow"><ArrowRight /></el-icon>
              </div>
              <div class="quick-item" @click="go('/after-sales')">
                <div class="quick-icon">
                  <el-icon><Service /></el-icon>
                </div>
                <div class="quick-text">
                  <div class="quick-title">售后工单</div>
                  <div class="quick-desc">工单流转与处理记录</div>
                </div>
                <el-icon class="quick-arrow"><ArrowRight /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getDashboardStats } from '../api/order'
import { useUserStore } from '../stores/user'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart, LineChart } from 'echarts/charts'
import { useRouter } from 'vue-router'
import {
  ArrowRight,
  CircleCheck,
  Clock,
  Coin,
  CreditCard,
  DataAnalysis,
  DataBoard,
  DataLine,
  Guide,
  List,
  PieChart as PieChartIcon,
  Service,
  Tools,
  Trophy,
  UserFilled
} from '@element-plus/icons-vue'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
} from 'echarts/components'

use([
  CanvasRenderer,
  BarChart,
  PieChart,
  LineChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
])

const userStore = useUserStore()
const router = useRouter()
const loading = ref(true)

const displayName = computed(() => {
  const user = userStore.currentUser || {}
  return user.realName || user.username || ''
})

const todayText = computed(() => {
  return new Date().toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    weekday: 'short'
  })
})

const isAdmin = computed(() => userStore.currentUser?.role === 'ADMIN')
const isInstallerOrAdmin = computed(() => {
  const role = userStore.currentUser?.role
  return role === 'INSTALLER' || role === 'ADMIN'
})

const go = (path) => {
  router.push(path)
}

const formatNumber = (value) => {
  const num = Number(value || 0)
  return num.toLocaleString('zh-CN')
}

const formatMoney = (value) => {
  const num = Number(value || 0)
  return `¥ ${num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

const stats = ref({
    pendingOrders: 0,
    finishedOrders: 0,
    totalOrders: 0,
    monthlySales: 0,
    monthlyPaidAmount: 0,
    orderTrend: [],
    brandDistribution: []
    , salesPerformance: []
})

const lineOption = ref({})
const pieOption = ref({})

onMounted(async () => {
    try {
        const res = await getDashboardStats()
        if (res.code === 200) {
            stats.value = res.data
            initCharts()
        }
    } catch (e) {
        console.error(e)
    } finally {
        loading.value = false
    }
})

const salesMaxAmount = computed(() => {
  const amounts = (stats.value.salesPerformance || []).map(item => Number(item.amount || 0))
  return Math.max(0, ...amounts)
})

const salesPercent = (amount) => {
  const max = salesMaxAmount.value
  if (!max) return 0
  const value = Number(amount || 0)
  const percent = (value / max) * 100
  return Math.max(0, Math.min(100, Math.round(percent)))
}

const initCharts = () => {
    // Line Chart
    const trendDates = stats.value.orderTrend.map(item => item.date)
    const trendCounts = stats.value.orderTrend.map(item => item.count)
    
    lineOption.value = {
        tooltip: {
            trigger: 'axis'
        },
        textStyle: {
            fontFamily: 'Inter, Segoe UI, system-ui, -apple-system, BlinkMacSystemFont, sans-serif'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: trendDates,
            axisLine: { lineStyle: { color: 'rgba(0,0,0,0.15)' } },
            axisTick: { show: false },
            axisLabel: { color: 'rgba(0,0,0,0.55)' }
        },
        yAxis: {
            type: 'value',
            splitLine: { lineStyle: { color: 'rgba(0,0,0,0.06)' } },
            axisLabel: { color: 'rgba(0,0,0,0.55)' }
        },
        series: [
            {
                name: '订单数',
                type: 'line',
                smooth: true,
                showSymbol: false,
                areaStyle: {
                    color: {
                        type: 'linear',
                        x: 0, y: 0, x2: 0, y2: 1,
                        colorStops: [
                            { offset: 0, color: 'rgba(99,102,241,0.35)' },
                            { offset: 1, color: 'rgba(99,102,241,0.01)' }
                        ]
                    }
                },
                lineStyle: { width: 3, color: '#6366F1' },
                itemStyle: { color: '#6366F1' },
                data: trendCounts
            }
        ]
    }
    
    // Pie Chart
    pieOption.value = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            textStyle: { color: 'rgba(0,0,0,0.65)' }
        },
        series: [
            {
                name: '品牌',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: 'rgba(255,255,255,0.85)',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 20,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: stats.value.brandDistribution
            }
        ]
    }
}
</script>

<style scoped>
.dashboard-page {
  position: relative;
  min-height: 100%;
  padding: 18px;
  border-radius: 18px;
  overflow: hidden;
  background: radial-gradient(1200px 600px at 10% 0%, rgba(99, 102, 241, 0.22), rgba(99, 102, 241, 0) 60%),
    radial-gradient(1000px 500px at 90% 10%, rgba(16, 185, 129, 0.18), rgba(16, 185, 129, 0) 55%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.35));
  border: 1px solid rgba(255, 255, 255, 0.7);
}

.dashboard-page::before {
  content: "";
  position: absolute;
  inset: 0;
  background-image: radial-gradient(rgba(99, 102, 241, 0.12) 1px, transparent 1px);
  background-size: 18px 18px;
  opacity: 0.35;
  pointer-events: none;
}

.hero {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 14px;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(17, 24, 39, 0.92), rgba(17, 24, 39, 0.74));
  box-shadow: 0 16px 40px rgba(17, 24, 39, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.12);
}

.hero-left {
  min-width: 0;
}

.hero-title {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.4px;
  color: rgba(255, 255, 255, 0.92);
}

.hero-subtitle {
  margin-top: 6px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.68);
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
}

.hero-primary :deep(.el-button__content) {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.btn-icon {
  margin-left: 2px;
}

.loading-skeleton {
  position: relative;
  margin-top: 18px;
}

.kpi-row,
.grid-row {
  position: relative;
  margin-top: 18px;
}

.kpi-row {
  row-gap: 18px;
}

.kpi-card,
.panel-card {
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.75);
  background: rgba(255, 255, 255, 0.72);
  backdrop-filter: blur(10px);
  box-shadow: 0 12px 30px rgba(17, 24, 39, 0.08);
}

.kpi-card {
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.kpi-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 18px 42px rgba(17, 24, 39, 0.12);
}

.kpi-inner {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.kpi-icon {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #111827;
  background: rgba(17, 24, 39, 0.06);
}

.kpi-meta {
  flex: 1;
  min-width: 0;
}

.kpi-title {
  font-size: 13px;
  color: rgba(17, 24, 39, 0.6);
}

.kpi-value {
  margin-top: 6px;
  font-size: 28px;
  font-weight: 800;
  color: rgba(17, 24, 39, 0.92);
  letter-spacing: 0.2px;
}

.kpi-value.money {
  font-size: 22px;
}

.kpi-foot {
  margin-top: 6px;
  font-size: 12px;
  color: rgba(17, 24, 39, 0.46);
}

.kpi-tag {
  align-self: flex-start;
  border: none;
}

.kpi-pending .kpi-icon {
  background: rgba(245, 158, 11, 0.14);
  color: #b45309;
}

.kpi-finished .kpi-icon {
  background: rgba(16, 185, 129, 0.14);
  color: #047857;
}

.kpi-total .kpi-icon {
  background: rgba(99, 102, 241, 0.14);
  color: #4338ca;
}

.kpi-money .kpi-icon {
  background: rgba(239, 68, 68, 0.14);
  color: #b91c1c;
}

.kpi-paid .kpi-icon {
  background: rgba(16, 185, 129, 0.14);
  color: #047857;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.panel-title {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
  color: rgba(17, 24, 39, 0.9);
}

.panel-hint {
  font-size: 12px;
  color: rgba(17, 24, 39, 0.45);
}

.chart-container {
  height: 300px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart {
  height: 100%;
  width: 100%;
}

.grid-col-right {
  margin-top: 18px;
}

@media (min-width: 1200px) {
  .grid-col-right {
    margin-top: 0;
  }
}

.money-text {
  font-weight: 700;
  color: rgba(17, 24, 39, 0.9);
}

.rank-table :deep(.el-progress-bar__outer) {
  background: rgba(17, 24, 39, 0.08);
}

.rank-table :deep(.el-progress-bar__inner) {
  background: linear-gradient(90deg, #6366f1, #10b981);
}

.quick-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.quick-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: 14px;
  background: rgba(17, 24, 39, 0.04);
  border: 1px solid rgba(17, 24, 39, 0.06);
  cursor: pointer;
  transition: transform 0.15s ease, background 0.15s ease, border-color 0.15s ease;
}

.quick-item:hover {
  transform: translateY(-2px);
  background: rgba(17, 24, 39, 0.06);
  border-color: rgba(99, 102, 241, 0.35);
}

.quick-icon {
  width: 40px;
  height: 40px;
  border-radius: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: rgba(99, 102, 241, 0.12);
  color: #4338ca;
}

.quick-text {
  flex: 1;
  min-width: 0;
}

.quick-title {
  font-weight: 700;
  color: rgba(17, 24, 39, 0.9);
}

.quick-desc {
  margin-top: 2px;
  font-size: 12px;
  color: rgba(17, 24, 39, 0.5);
}

.quick-arrow {
  color: rgba(17, 24, 39, 0.45);
}
</style>
