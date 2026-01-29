import request from '@/utils/request'

export function listCustomers(params) {
  return request({
    url: '/customer/list',
    method: 'get',
    params
  })
}

export function getCustomerDetail(id) {
  return request({
    url: `/customer/detail/${id}`,
    method: 'get'
  })
}

export function exportCustomers(data) {
  return request({
    url: '/customer/export',
    method: 'post',
    data
  })
}
