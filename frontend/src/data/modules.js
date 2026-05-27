export const modules = [
  {
    key: "stock-registry",
    name: "股票主数据",
    purpose: "统一维护 A 股和港股股票身份、代码和基础资料。",
    dependsOn: [],
    deliverables: [
      "统一股票主标识 `market + stock_code`",
      "股票主表与基础索引",
      "本地股票列表、搜索和详情接口骨架"
    ],
    outOfScope: [
      "不在这里实现外部数据拉取",
      "不在这里实现财务筛选逻辑"
    ]
  },
  {
    key: "data-ingestion-sync",
    name: "数据拉取与同步",
    purpose: "按指定代码显式触发同步，并按数据源顺序兜底。",
    dependsOn: ["stock-registry"],
    deliverables: [
      "按指定股票触发同步",
      "数据源适配器与兜底执行顺序",
      "同步日志与失败摘要"
    ],
    outOfScope: [
      "不在查询接口里自动回源",
      "不把定时同步做成一期主链路"
    ]
  },
  {
    key: "financial-screening",
    name: "财务筛选",
    purpose: "维护历史财务和筛选快照，支撑多指标组合筛选。",
    dependsOn: ["stock-registry", "data-ingestion-sync"],
    deliverables: [
      "历史财务表模型",
      "筛选快照表模型",
      "多指标筛选与个股历史查询接口骨架"
    ],
    outOfScope: [
      "不做自动估值模型",
      "不把 PDF 解析结果直接写入正式快照"
    ]
  },
  {
    key: "announcement-center",
    name: "公告中心",
    purpose: "维护公告元数据和原文链接，支持本地查询与人工解析入口。",
    dependsOn: ["stock-registry"],
    deliverables: [
      "公告元数据模型",
      "公告列表、类型和时间筛选",
      "原文链接与来源字段规范"
    ],
    outOfScope: [
      "不默认缓存 PDF 原文",
      "不强依赖 data-ingestion-sync"
    ]
  },
  {
    key: "deployment",
    name: "部署",
    purpose: "提供本地优先的 Docker Compose 交付结构。",
    dependsOn: [],
    deliverables: [
      "后端、前端、MySQL 的 Compose 骨架",
      "环境变量模板",
      "README 本地启动说明"
    ],
    outOfScope: [
      "不把公网部署作为一期前提",
      "不做复杂运维体系"
    ]
  }
];

export const projectSummary = {
  stage: "规划与骨架阶段",
  architecture: "单 Spring Boot 后端 + Vue 3 前端 + MySQL",
  queryMode: "默认只查本地库，本地缺失时不自动回源",
  sources: ["东方财富", "巨潮资讯", "HKEXnews", "Tushare(预留)"],
  deployment: "本地 Windows 机器优先，后续可迁移到 NAS 或 VPS"
};

export function findModule(moduleKey) {
  return modules.find((item) => item.key === moduleKey);
}
