import { createRouter, createWebHistory } from 'vue-router'
import StockList from '../views/StockList.vue'

const routes = [
  { path: '/', redirect: '/stocks' },
  { path: '/stocks', name: 'StockList', component: StockList },
  { path: '/stocks/:code', name: 'StockDetail', component: () => import('../views/StockDetail.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
