import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import Login from '../views/Login.vue'
import OrderList from '../views/OrderList.vue'
import BrandList from '../views/BrandList.vue'
import UserList from '../views/UserList.vue'
import OrderDetail from '../views/OrderDetail.vue'
import OrderPayments from '../views/OrderPayments.vue'
import Dashboard from '../views/Dashboard.vue'
import Logs from '../views/Logs.vue'
import AfterSalesList from '../views/AfterSalesList.vue'
import CustomerList from '../views/CustomerList.vue'
import RemeasureTaskList from '../views/RemeasureTaskList.vue'
import MainLayout from '../layout/MainLayout.vue'
import PaymentDetail from '../views/PaymentDetail.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
      },
      {
        path: 'orders',
        name: 'OrderList',
        component: OrderList,
        meta: { requiresAuth: true }
      },
      {
        path: 'order/detail/:id',
        name: 'OrderDetail',
        component: OrderDetail,
        meta: { requiresAuth: true }
      },
      {
        path: 'order/payments/:id',
        name: 'OrderPayments',
        component: OrderPayments,
        meta: { requiresAuth: true }
      },
      {
        path: 'order/payment/:id',
        name: 'PaymentDetail',
        component: PaymentDetail,
        meta: { requiresAuth: true }
      },
      {
        path: 'brands',
        name: 'BrandList',
        component: BrandList,
        meta: { requiresAuth: true, role: 'ADMIN' }
      },
      {
        path: 'users',
        name: 'UserList',
        component: UserList,
        meta: { requiresAuth: true, role: 'ADMIN' }
      }
      ,
      {
        path: 'customers',
        name: 'CustomerList',
        component: CustomerList,
        meta: { requiresAuth: true }
      },
      {
        path: 'after-sales',
        name: 'AfterSalesList',
        component: AfterSalesList,
        meta: { requiresAuth: true }
      },
      {
        path: 'remeasure-tasks',
        name: 'RemeasureTaskList',
        component: RemeasureTaskList,
        meta: { requiresAuth: true }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: Logs,
        meta: { requiresAuth: true, role: 'ADMIN' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    const isAuthenticated = !!userStore.currentUser?.id
    
    if (to.meta.requiresAuth && !isAuthenticated) {
        next('/login')
    } else if (to.meta.role && userStore.currentUser?.role !== to.meta.role) {
        next('/orders') // or 403
    } else {
        next()
    }
})

export default router
