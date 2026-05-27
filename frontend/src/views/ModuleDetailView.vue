<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import { findModule } from "../data/modules";

const route = useRoute();

const moduleDetail = computed(() => findModule(route.params.moduleKey));
</script>

<template>
  <section v-if="moduleDetail" class="workspace-section">
    <header class="section-header">
      <div>
        <p class="section-kicker">Capability Detail</p>
        <h2>{{ moduleDetail.name }}</h2>
      </div>
      <el-tag type="warning" effect="plain">Scaffold Only</el-tag>
    </header>

    <article class="hero-panel">
      <p class="hero-copy">{{ moduleDetail.purpose }}</p>
    </article>
  </section>

  <section v-if="moduleDetail" class="workspace-section two-column">
    <article class="info-panel">
      <h3>依赖</h3>
      <p>{{ moduleDetail.dependsOn.length ? moduleDetail.dependsOn.join(", ") : "无" }}</p>
    </article>

    <article class="info-panel">
      <h3>当前状态</h3>
      <p>对应 OpenSpec change 已创建，尚未进入逐 task 实现。</p>
    </article>
  </section>

  <section v-if="moduleDetail" class="workspace-section two-column">
    <article class="info-panel">
      <h3>计划交付</h3>
      <ul>
        <li v-for="item in moduleDetail.deliverables" :key="item">{{ item }}</li>
      </ul>
    </article>

    <article class="info-panel">
      <h3>暂不包含</h3>
      <ul>
        <li v-for="item in moduleDetail.outOfScope" :key="item">{{ item }}</li>
      </ul>
    </article>
  </section>

  <section v-else class="workspace-section">
    <el-empty description="未找到对应模块" />
  </section>
</template>
