import request from '@/utils/request'

export function createPayment(data) {
  return request({
    url: '/payment',
    method: 'post',
    data
  })
}

export function listPayments(orderId) {
  return request({
    url: `/payment/order/${orderId}`,
    method: 'get'
  })
}
