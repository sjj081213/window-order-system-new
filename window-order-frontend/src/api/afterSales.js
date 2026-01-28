import request from '@/utils/request'

export function listAfterSales(data) {
  return request({
    url: '/after-sales/list',
    method: 'post',
    data
  })
}

export function createAfterSales(data) {
  return request({
    url: '/after-sales/create',
    method: 'post',
    data
  })
}

export function updateAfterSales(data) {
  return request({
    url: '/after-sales/update',
    method: 'post',
    data
  })
}

export function getAfterSalesDetail(id) {
  return request({
    url: `/after-sales/detail/${id}`,
    method: 'get'
  })
}

export function deleteAfterSales(id) {
  return request({
    url: `/after-sales/${id}`,
    method: 'delete'
  })
}
