import { createRouter, createWebHistory } from "vue-router";
import OverviewView from "../views/OverviewView.vue";
import ModuleDetailView from "../views/ModuleDetailView.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "overview",
      component: OverviewView
    },
    {
      path: "/modules/:moduleKey",
      name: "module-detail",
      component: ModuleDetailView
    }
  ]
});

export default router;
