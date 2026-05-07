<template>
  <div v-if="stock">
    <el-button @click="$router.back()" style="margin-bottom: 16px">返回</el-button>
    <el-descriptions title="股票详情" border>
      <el-descriptions-item label="股票代码">{{ stock.stockCode }}</el-descriptions-item>
      <el-descriptions-item label="股票名称">{{ stock.stockName }}</el-descriptions-item>
      <el-descriptions-item label="市场">{{ stock.market }}</el-descriptions-item>
      <el-descriptions-item label="行业">{{ stock.industry }}</el-descriptions-item>
      <el-descriptions-item label="上市日期">{{ stock.listDate }}</el-descriptions-item>
    </el-descriptions>
  </div>
  <div v-else>
    <el-button @click="$router.back()">返回</el-button>
    <p>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getStockDetail } from '../api'

const route = useRoute()
const stock = ref(null)

onMounted(async () => {
  const res = await getStockDetail(route.params.code)
  stock.value = res.data
})
</script>
