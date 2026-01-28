import request from '@/utils/request'

export const listLogs = (data) => request.post('/logs/list', data)
