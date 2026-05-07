import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

export function getStockList(market) {
  return api.get('/stock/list', { params: { market } })
}

export function getStockDetail(code) {
  return api.get(`/stock/${code}`)
}

export function refreshStockList(market) {
  return api.post('/stock/refresh', null, { params: { market } })
}

export default api
