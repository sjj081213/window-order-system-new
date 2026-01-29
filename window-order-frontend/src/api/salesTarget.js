import request from '@/utils/request'

export function setTarget(data) {
  return request({
    url: '/sales-target/set',
    method: 'post',
    data
  })
}

export function listTargets(params) {
  return request({
    url: '/sales-target/list',
    method: 'get',
    params
  })
}

export function exportTargets(data) {
  return request({
    url: '/sales-target/export',
    method: 'post',
    data
  })
}
