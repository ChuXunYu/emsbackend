# 东软环保公众监督系统 - Vue 界面设计规约

## 1. 设计原则
- **一致性**: 所有界面元素、组件和交互行为应保持高度一致。
- **清晰性**: 界面布局、信息层级和操作指引清晰明确，避免用户困惑。
- **效率性**: 简化操作流程，减少不必要的点击和输入，让用户高效完成任务。
- **响应式**: 确保在不同尺寸的设备上（特别是PC端主流分辨率）都能获得良好的视觉和交互体验。
- **美观性**: 遵循现代化的审美标准，界面简洁、美观、专业。

## 2. 颜色系统 (Color Palette)
颜色方案以"科技蓝"为主色调，辅以中性灰和功能色，营造专业、冷静、可靠的视觉感受。

- **主色 (Primary)**
  - `Brand Blue`: `#409EFF` - 用于关键操作按钮、Logo、导航高亮等。
- **功能色 (Functional)**
  - `Success Green`: `#67C23A` - 用于成功提示、操作完成状态。
  - `Warning Orange`: `#E6A23C` - 用于警告信息、需要用户注意的操作。
  - `Danger Red`: `#F56C6C` - 用于错误提示、删除、高危操作。
  - `Info Gray`: `#909399` - 用于普通信息、辅助说明。
- **中性色 (Neutral)**
  - `Text Primary`: `#303133` - 主要文字颜色。
  - `Text Regular`: `#606266` - 常规文字、次要信息。
  - `Text Secondary`: `#909399` - 辅助、占位文字。
  - `Border Color`: `#DCDFE6` - 边框、分割线。
  - `Background`: `#F5F7FA` - 页面背景色。
  - `Container Background`: `#FFFFFF` - 卡片、表格等容器背景色。

## 3. 字体与排版 (Typography)
- **字体族**: `Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif`
- **字号体系**:
  - `H1 / 页面主标题`: 24px (Bold)
  - `H2 / 区域标题`: 20px (Bold)
  - `H3 / 卡片/弹窗标题`: 18px (Medium)
  - `正文/表格内容`: 14px (Regular)
  - `辅助/说明文字`: 12px (Regular)
- **行高**: `1.6` - `1.8`，保证阅读舒适性。
- **段落间距**: `16px`

## 4. 布局与间距 (Layout & Spacing)
- **主布局**: 采用经典的侧边栏导航 + 内容区的布局方式。
  - `NEPM (管理端)` / `NEPV (大屏端)`: 左侧为固定菜单栏，右侧为内容展示区。
  - `NEPS (公众端)` / `NEPG (网格员端)`: 根据移动端优先原则，可采用底部Tab栏或顶部导航。
- **间距单位**: 以 `8px` 为基础栅格单位。
  - `xs`: 4px
  - `sm`: 8px
  - `md`: 16px
  - `lg`: 24px
  - `xl`: 32px
- **页面内边距 (Padding)**: 主要内容区域应有 `24px` 的内边距。
- **组件间距 (Gap)**: 卡片、表单项之间保持 `16px` 或 `24px` 的间距。

## 5. 核心组件样式规约

### 5.1 按钮 (Button)
- **主按钮 (Primary)**: `background: #409EFF`, `color: #FFFFFF`。用于页面核心操作。
- **次按钮 (Default)**: `border: 1px solid #DCDFE6`, `background: #FFFFFF`。用于非核心或次要操作。
- **危险按钮 (Danger)**: `background: #F56C6C`, `color: #FFFFFF`。用于删除等高危操作。
- **尺寸**: 高度分为 `大 (40px)`, `中 (32px)`, `小 (24px)` 三档。

### 5.2 表格 (Table)
- **表头**: 背景色 `#FAFAFA`, 字体加粗。
- **行**: 偶数行可设置 `#F5F7FA` 的背景色以增强区分度 (斑马纹)。
- **操作区**: 表格最后一列通常为操作区，放置`查看`、`编辑`、`删除`等按钮，建议使用文字按钮或小尺寸图标按钮。

### 5.3 表单 (Form)
- **标签 (Label)**: 右对齐，与输入框保持 `8px` 距离。
- **输入框 (Input)**: 统一高度，`hover` 和 `focus` 状态有明显视觉反馈 (边框颜色变为 `#409EFF`)。
- **必填项**: 在标签前用红色 `*` 标示。

### 5.4 卡片 (Card)
- **样式**: `background: #FFFFFF`, `border-radius: 8px`, `box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)`。
- **内边距**: `20px`。

## 6. 编码与实现规约
- **组件化**: 遵循Vue的组件化思想，将可复用的UI和逻辑封装成组件。
- **命名规范**:
  - 组件文件名: `PascalCase` (e.g., `UserTable.vue`)
  - props/data/methods: `camelCase` (e.g., `tableData`)
- **状态管理**: 使用 `Pinia` 统一管理全局状态，如用户信息、角色权限等。
- **API请求**: 封装 `Axios`，统一处理请求头、响应拦截和错误处理。
- **TypeScript**: 全程使用TypeScript，为`props`, `emits`, `data` 和 `store` 提供明确的类型定义。
- **代码风格**: 遵循 `ESLint + Prettier` 的配置，确保代码风格统一。 