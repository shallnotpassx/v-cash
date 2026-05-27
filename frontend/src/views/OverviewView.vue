<script setup>
import { modules, projectSummary } from "../data/modules";
</script>

<template>
  <section class="workspace-section">
    <header class="section-header">
      <div>
        <p class="section-kicker">Project Overview</p>
        <h2>v-cash 当前骨架</h2>
      </div>
      <el-tag type="info" effect="plain">Spec Ready</el-tag>
    </header>

    <div class="metric-grid">
      <article class="metric-panel">
        <span class="metric-label">当前阶段</span>
        <strong>{{ projectSummary.stage }}</strong>
      </article>
      <article class="metric-panel">
        <span class="metric-label">系统形态</span>
        <strong>{{ projectSummary.architecture }}</strong>
      </article>
      <article class="metric-panel">
        <span class="metric-label">查询策略</span>
        <strong>{{ projectSummary.queryMode }}</strong>
      </article>
    </div>
  </section>

  <section class="workspace-section">
    <header class="section-header">
      <div>
        <p class="section-kicker">Key Decisions</p>
        <h2>已经定下来的约束</h2>
      </div>
    </header>

    <div class="decision-grid">
      <article class="info-panel">
        <h3>免费源优先</h3>
        <ul>
          <li v-for="source in projectSummary.sources" :key="source">{{ source }}</li>
        </ul>
      </article>

      <article class="info-panel">
        <h3>交付方式</h3>
        <p>{{ projectSummary.deployment }}</p>
      </article>
    </div>
  </section>

  <section class="workspace-section">
    <header class="section-header">
      <div>
        <p class="section-kicker">Capability Map</p>
        <h2>能力域拆分</h2>
      </div>
    </header>

    <div class="module-grid">
      <router-link
        v-for="module in modules"
        :key="module.key"
        :to="`/modules/${module.key}`"
        class="module-card"
      >
        <div class="module-card-header">
          <h3>{{ module.name }}</h3>
          <el-tag type="success" effect="plain">Proposed</el-tag>
        </div>
        <p>{{ module.purpose }}</p>
        <div class="dependency-line">
          <span>依赖：</span>
          <strong>{{ module.dependsOn.length ? module.dependsOn.join(", ") : "无" }}</strong>
        </div>
      </router-link>
    </div>
  </section>
</template>
