<template>
  <div class="header-bar" :class="{ 'no-shadow': !showShadow }">
    <div class="brand" v-if="showBrand">
      <el-icon class="brand-icon"><House /></el-icon>
      <span class="brand-name">窗户订单管理</span>
    </div>
    <div class="right-menu">
      <div class="user-info">
        <el-dropdown>
          <span class="el-dropdown-link">
            <el-avatar :size="32" class="user-avatar" :icon="UserFilled" />
            <span class="username">{{ userStore.currentUser.realName || userStore.currentUser.username }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { House, UserFilled, ArrowDown, SwitchButton } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'

const props = defineProps({
  showBrand: {
    type: Boolean,
    default: true
  },
  showShadow: {
    type: Boolean,
    default: false
  }
})

const router = useRouter()
const userStore = useUserStore()

const logout = () => {
  userStore.clearUser()
  router.push('/login')
}
</script>

<style scoped>
.header-bar {
  background-color: transparent;
  height: 100%;
  display: flex;
  justify-content: flex-end; /* Align to right by default if brand is hidden */
  align-items: center;
  padding: 0 24px;
  flex: 1;
}

.brand {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-right: auto; /* Push right menu to end */
}

.brand-icon {
  margin-right: 10px;
  font-size: 24px;
  color: #409EFF;
}

.right-menu {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  outline: none;
}

.username {
  margin: 0 8px;
  font-size: 14px;
  color: #606266;
}
</style>
