<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import { modules } from "./data/modules";

const route = useRoute();
const activeMenu = computed(() => route.path);
</script>

<template>
  <div class="app-shell">
    <aside class="app-sidebar">
      <div class="brand-block">
        <p class="brand-kicker">v-cash</p>
        <h1>项目骨架</h1>
        <p class="brand-summary">
          当前仓库处于规划和骨架阶段，默认采用本地优先、手动同步、单后端单前端的交付方式。
        </p>
      </div>

      <el-menu :default-active="activeMenu" class="nav-menu" router>
        <el-menu-item index="/">项目总览</el-menu-item>
        <el-menu-item
          v-for="module in modules"
          :key="module.key"
          :index="`/modules/${module.key}`"
        >
          {{ module.name }}
        </el-menu-item>
      </el-menu>
    </aside>

    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>
