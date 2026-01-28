import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const redirectToLogin = (message) => {
    ElMessage.error(message)
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    const currentPath = router.currentRoute && router.currentRoute.value && router.currentRoute.value.path
    if (currentPath !== '/login') {
        router.push('/login')
    }
}

const request = axios.create({
    baseURL: '/api',
    timeout: 10000
})

request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers = config.headers || {}
        config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
}, error => {
    return Promise.reject(error)
})

request.interceptors.response.use(response => {
    // Pass through for file download
    if (response.config && response.config.responseType === 'blob') {
        return response
    }
    const res = response.data
    if (res && res.code === 200) {
        return res
    } else if (res && (res.code === 401 || res.code === 403)) {
        redirectToLogin('登录过期，请重新登录')
        return Promise.reject(new Error((res && res.message) || 'Unauthorized'))
    } else {
        ElMessage.error((res && res.message) || 'Error')
        return Promise.reject(new Error((res && res.message) || 'Error'))
    }
}, error => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        redirectToLogin('登录过期，请重新登录')
    } else if (error.response && error.response.status === 500) {
        ElMessage.error('系统异常，请稍后再试。')
    } else {
        ElMessage.error(error.message || 'Network Error')
    }
    return Promise.reject(error)
})

export default request
