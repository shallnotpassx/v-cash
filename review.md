  结论

  当前项目的 docs/openspec/ 已经具备了基本框架，不能算“没上 OpenSpec”；但离“规范、可持续、可驱动后续开发”还有一段距离。整体判断是：

  - 结构上：基本规范
  - 内容上：部分规范
  - 流程闭环上：明显不完整

  如果只问“能不能继续用”，答案是能。
  如果问“现在这套 OpenSpec 够不够稳”，答案是不够，后面最好补齐。

  做得对的部分

  - 目录分层基本正确：有 specs/ 和 changes/，并且 change 下拆了 proposal.md、design.md、tasks.md。
  - 能力切分基本合理：stock-basic、stock-refresh、stock-search、stock-sync-automation、financial-data、announcement-data、docker-deployment 这些 capability 名称清晰。
  - 需求写法基本接近 spec 风格：大多数 spec.md 都有 Purpose、Requirements、Scenario，已经不是随手记笔记的状态。
  - phase 化 change 也基本符合项目推进节奏，和 docs/PLAN.md 能对应上。

  主要问题

  1. change 和 spec 的职责边界还不够清楚

  现在很多 changes/<phase>/specs/.../spec.md 看起来是在“复制一份最终 spec”，而不是清楚表达“这个 change 对基线 spec 做了什么增量”。这会带来两个问题：

  - 后续维护时，docs/openspec/specs/... 和 docs/openspec/changes/.../specs/... 容易漂移
  - 归档后很难判断应该保留哪一份作为真相来源

  典型例子是 phase-0-stock-basic 同时带了：

  - docs/openspec/specs/stock-basic/spec.md
  - docs/openspec/changes/phase-0-stock-basic/specs/stock-basic/spec.md
  - docs/openspec/specs/stock-refresh/spec.md
  - docs/openspec/changes/phase-0-stock-basic/specs/stock-refresh/spec.md

  如果这是 OpenSpec 的变更提案阶段，change 里的 spec 应该强调“新增/修改了什么要求”，而不是像最终版一样平铺一遍。

  2. 没有归档闭环

  从提交历史看，项目已经实现了不少东西，至少 phase-0-stock-basic 对应内容已经落地，PLAN.md 也标记 Phase 0 完成。但 changes/ 下面没有看到归档后的痕迹，仍然保留在活跃 change 区。

  这意味着流程停在“写了 spec 和 tasks，也做了实现”，但没有完成 OpenSpec 最关键的一步之一：把已实现 change 合并回基线 spec，并归档 change。

  这会直接导致：

  - 哪些变更还在进行中，不清楚
  - 哪些 spec 已成为现状，不清楚
  - 后续继续提 change 时，基线容易失真

  phase-0-stock-basic 这一项严格说属于 应该归档但尚未归档。

  3. tasks.md 更像施工清单，不完全像可验收任务

  很多 tasks.md 写的是实现动作，但没有明确验收结果。比如 docs/openspec/changes/phase-1-search-sync/tasks.md 里的：

  - StockApplicationService.listFromDb 支持 keyword 参数
  - StockController.list() 新增 keyword 查询参数

  这类任务工程上没问题，但从 spec-driven 角度还差一点“完成标准”。例如没有写清：

  - 是否要求 market + keyword 组合过滤
  - 空关键字如何处理
  - 定时同步失败如何观测
  - 前端搜索防抖是否必须 300ms，还是实现建议

  也就是说，任务能指导编码，但不够支撑 review 和验收。

  4. spec 的验收边界偏薄

  现在的 spec.md 多数有 happy path，但缺少一些关键边界，尤其是错误处理、空结果、幂等性、性能/规模、调度行为。

  例如：

  - docs/openspec/specs/stock-search/spec.md 没写空关键字、大小写、无结果时返回什么
  - docs/openspec/specs/stock-sync-automation/spec.md 没写重复执行、并发执行防护、异常记录粒度
  - docs/openspec/specs/financial-data/spec.md 没写数据源为空、股票不存在、历史数据 upsert 冲突时的预期
  - docs/openspec/specs/announcement-data/spec.md 没写分页、去重键、类型标准化规则

  这些不是“必须一上来全写完”，但如果目标是规范的 OpenSpec，这些至少要在后续补齐到能判定对错的程度。

  5. design.md 偏“实现说明”，还不够“设计决策记录”

  比如 docs/openspec/changes/phase-0-stock-basic/design.md 已经有 Goals / Non-Goals、Decisions、Risks / Trade-offs，这是好的；但它还缺几类常见的设计关键信息：

  - 关键接口边界和调用链
  - 数据模型与唯一键/更新策略
  - 失败回退的判定标准
  - 哪些地方故意留到后续 phase，而不是遗漏

  现在这份 design 能说明“想怎么做”，但还不够支持别人不看代码就理解方案约束。

  6. OpenSpec 路径约定在文档里有一处不一致

  AGENTS.md 的“任务完成检查清单”里写的是：

  - openspec/changes/<name>/tasks.md

  但项目实际路径已经迁到 docs/openspec/...，而且同一份文件后文也在写 docs/openspec/。这属于小问题，但它会误导后续维护。

  7. 当前 spec 集合还缺一个“项目级约束”承载处

  现在很多跨 capability 的约束散落在 README 和 AGENTS 里，比如：

  - DDD 分层依赖规则
  - 适配器策略 + 兜底模式
  - upsert 约定
  - 逻辑删除
  - BigDecimal
  - 调度行为和配置方式

  这些当然可以放在 README/AGENTS，但如果 OpenSpec 要成为主要规范来源，最好有一个明确方式承接“跨能力通用约束”。否则每个 capability spec 都会重复写，或者干脆漏写。

  我认为必须补的

  这些不补，后面 OpenSpec 会越来越难维护：

  - phase-0-stock-basic 这类已完成 change 的归档闭环
  - change spec 与 base spec 的职责分离
  - AGENTS.md 中 OpenSpec 路径引用修正为 docs/openspec/...
  - 后续新增 change 时，tasks.md 增加更明确的验收标准

  我认为建议补的

  这些不影响马上继续做功能，但会明显提升规范性：

  - 为各 capability 补错误场景和边界场景
  - 为 design.md 补接口边界、数据约束、失败策略
  - 增加项目级约束的统一 spec 或统一设计约定入口
  - 给每个 phase 的 change 标明状态，比如 proposed / in-progress / completed / archived

  一句话判断

  现在这套 OpenSpec 不是“乱”，而是“已经起步，但还停在第一轮建模和任务分解阶段”。最大的问题不是缺文件，而是缺流程闭环和基线治理。