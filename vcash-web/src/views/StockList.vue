<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <h3>股票列表</h3>
      <el-button type="primary" @click="handleRefresh">刷新</el-button>
    </div>
    <el-table :data="stocks" stripe @row-click="goDetail" style="cursor: pointer">
      <el-table-column prop="stockCode" label="代码" width="120" />
      <el-table-column prop="stockName" label="名称" width="200" />
      <el-table-column prop="market" label="市场" width="100" />
      <el-table-column prop="industry" label="行业" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getStockList, refreshStockList } from '../api'

const router = useRouter()
const stocks = ref([])

async function fetchData() {
  const res = await getStockList()
  stocks.value = res.data
}

async function handleRefresh() {
  await refreshStockList()
  await fetchData()
}

function goDetail(row) {
  router.push(`/stocks/${row.stockCode}`)
}

onMounted(fetchData)
</script>
