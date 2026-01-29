import request from '@/utils/request'

export const listRemeasureTasks = (data) => request.post('/remeasure/list', data)
export const assignRemeasureTask = (data) => request.post('/remeasure/assign', data)
export const submitRemeasureTask = (data) => request.post('/remeasure/submit', data)
export const getRemeasureTask = (id) => request.get(`/remeasure/detail/${id}`)
export const getRemeasureTaskByOrderId = (orderId) => request.get(`/remeasure/order/${orderId}`)
export const exportRemeasureTasks = (data) => request.post('/remeasure/export', data)
