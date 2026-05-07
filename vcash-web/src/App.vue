<template>
  <div id="app-container">
    <el-container style="height: 100vh">
      <el-aside width="200px">
        <el-menu router :default-active="$route.path">
          <el-menu-item index="/stocks">
            <el-icon><List /></el-icon>
            <span>股票列表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <h2>v-cash</h2>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { List } from '@element-plus/icons-vue'
</script>

<style>
body { margin: 0; }
#app-container .el-header { background: #f5f7fa; display: flex; align-items: center; border-bottom: 1px solid #e4e7ed; }
#app-container .el-header h2 { margin: 0; font-size: 18px; }
#app-container .el-aside { background: #304156; }
#app-container .el-aside .el-menu { border-right: none; }
</style>
