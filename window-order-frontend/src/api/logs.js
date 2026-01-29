import request from '@/utils/request'

export const listLogs = (data) => request.post('/logs/list', data)

export const exportLogs = (data) => request.post('/logs/export', data)
