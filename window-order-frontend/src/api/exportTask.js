import request from '@/utils/request'

export function listTasks(params) {
  return request({
    url: '/export-task/list',
    method: 'post',
    data: params
  })
}
