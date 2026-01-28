import request from '@/utils/request'

export const login = (data) => request.post('/auth/login', data)
export const logout = () => {
    // maybe call backend logout
    localStorage.removeItem('user')
}
