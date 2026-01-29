import request from '@/utils/request'

export function listTasks() {
  return request({
    url: '/export-task/list',
    method: 'get'
  })
}

export function createTestTask() {
  return request({
    url: '/export-task/create-test',
    method: 'post'
  })
}
