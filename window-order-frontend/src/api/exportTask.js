import request from '@/utils/request'

export function listTasks() {
  return request({
    url: '/export-task/list',
    method: 'get'
  })
}
