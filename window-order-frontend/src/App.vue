<template>
  <router-view></router-view>
</template>

<script setup>
import { onMounted, onUnmounted, watch } from 'vue'
import { useUserStore } from './stores/user'
import { ElNotification } from 'element-plus'

const userStore = useUserStore()
let socket = null
let reconnectTimer = null
const RECONNECT_INTERVAL = 5000

const initWebSocket = () => {
    if (!userStore.currentUser.id) return
    
    // Check if already connected or connecting
    if (socket && (socket.readyState === WebSocket.OPEN || socket.readyState === WebSocket.CONNECTING)) return
    
    const userId = userStore.currentUser.id
    const role = userStore.currentUser.role
    
    // Assuming backend is localhost:18080
    // In production, use window.location.host
    const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws'
    // Use fixed port 18080 as per vite config proxy target, or dynamic if deployed
    const wsUrl = `${protocol}://localhost:18080/ws/${userId}/${role}`
    
    socket = new WebSocket(wsUrl)
    
    socket.onopen = () => {
        console.log('WebSocket Connected')
        if (reconnectTimer) {
            clearInterval(reconnectTimer)
            reconnectTimer = null
        }
    }
    
    socket.onmessage = (event) => {
        try {
            const data = JSON.parse(event.data)
            // Expected format: { title, message, type }
            ElNotification({
                title: data.title || 'Notification',
                message: data.message || '',
                type: data.type || 'info',
                duration: 5000
            })
        } catch (e) {
            console.error('Failed to parse WS message', e)
        }
    }
    
    socket.onerror = (error) => {
        console.error('WebSocket Error', error)
    }
    
    socket.onclose = () => {
        console.log('WebSocket Closed')
        socket = null
        // Reconnect logic
        if (userStore.currentUser.id && !reconnectTimer) {
             console.log(`Attempting to reconnect in ${RECONNECT_INTERVAL}ms...`)
             reconnectTimer = setInterval(() => {
                 initWebSocket()
             }, RECONNECT_INTERVAL)
        }
    }
}

const closeWebSocket = () => {
    if (reconnectTimer) {
        clearInterval(reconnectTimer)
        reconnectTimer = null
    }
    if (socket) {
        socket.close()
        socket = null
    }
}

// Watch user login state
watch(() => userStore.currentUser.id, (newVal) => {
    if (newVal) {
        initWebSocket()
    } else {
        closeWebSocket()
    }
})

onMounted(() => {
    if (userStore.currentUser.id) {
        initWebSocket()
    }
})

onUnmounted(() => {
    closeWebSocket()
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 0;
  width: 100%;
  height: 100vh;
}
body {
  margin: 0;
  padding: 0;
}
</style>
