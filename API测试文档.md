# EMS 后端 API 测试文档

本文档基于项目源代码生成，包含了所有后端 API 接口的详细信息，用于开发和测试。

---

## 目录
1.  [认证模块 (Auth)](#1-认证模块-auth)
2.  [仪表盘模块 (Dashboard)](#2-仪表盘模块-dashboard)
3.  [反馈模块 (Feedback)](#3-反馈模块-feedback)
4.  [文件模块 (File)](#4-文件模块-file)
5.  [网格模块 (Grid)](#5-网格模块-grid)
6.  [网格员任务模块 (Worker)](#6-网格员任务模块-worker)
7.  [地图模块 (Map)](#7-地图模块-map)
8.  [路径规划模块 (Pathfinding)](#8-路径规划模块-pathfinding)
9.  [人员管理模块 (Personnel)](#9-人员管理模块-personnel)
10. [个人资料模块 (Me)](#10-个人资料模块-me)
11. [公共接口模块 (Public)](#11-公共接口模块-public)
12. [主管模块 (Supervisor)](#12-主管模块-supervisor)
13. [任务分配模块 (Tasks)](#13-任务分配模块-tasks)
14. [任务管理模块 (Management)](#14-任务管理模块-management)

---

## 1. 认证模块 (Auth)
**基础路径**: `/api/auth`

### 1.1 用户注册
- **功能描述**: 注册一个新用户。
- **请求路径**: `/api/auth/signup`
- **请求方法**: `POST`
- **所需权限**: 无
- **请求体**: `SignUpRequest`
- **示例请求**:
  ```json
  {
      "name": "测试用户",
      "email": "test@example.com",
      "phone": "13800138000",
      "password": "password123",
      "verificationCode": "123456",
      "role": "GRID_WORKER"
  }
  ```

### 1.2 用户登录
- **功能描述**: 用户登录并获取 JWT Token。
- **请求路径**: `/api/auth/login`
- **请求方法**: `POST`
- **所需权限**: 无
- **请求体**: `LoginRequest`
- **示例请求**:
  ```json
  {
      "email": "admin@aizhangz.top",
      "password": "Admin@123"
  }
  ```

### 1.3 发送验证码
- **功能描述**: 请求发送验证码到指定邮箱。
- **请求路径**: `/api/auth/send-verification-code`
- **请求方法**: `POST`
- **所需权限**: 无
- **查询参数**: `email` (string, 必需)
- **示例请求**: `POST /api/auth/send-verification-code?email=test@example.com`

### 1.4 请求重置密码
- **功能描述**: 请求发送重置密码的链接或令牌。
- **请求路径**: `/api/auth/request-password-reset`
- **请求方法**: `POST`
- **所需权限**: 无
- **请求体**: `PasswordResetRequest`
- **示例请求**:
  ```json
  {
      "email": "test@example.com"
  }
  ```

### 1.5 重置密码
- **功能描述**: 使用令牌重置密码。
- **请求路径**: `/api/auth/reset-password`
- **请求方法**: `POST`
- **所需权限**: 无
- **请求体**: `PasswordResetDto`
- **示例请求**:
  ```json
  {
      "token": "some-reset-token",
      "newPassword": "newPassword123"
  }
  ```

---

## 2. 仪表盘模块 (Dashboard)
**基础路径**: `/api/dashboard`
**所需权限**: `DECISION_MAKER`

### 2.1 获取仪表盘统计数据
- **请求路径**: `/api/dashboard/stats`
- **请求方法**: `GET`

### 2.2 获取 AQI 分布
- **请求路径**: `/api/dashboard/reports/aqi-distribution`
- **请求方法**: `GET`

### 2.3 获取污染超标月度趋势
- **请求路径**: `/api/dashboard/reports/pollution-trend`
- **请求方法**: `GET`

### 2.4 获取网格覆盖率
- **请求路径**: `/api/dashboard/reports/grid-coverage`
- **请求方法**: `GET`

### 2.5 获取反馈热力图数据
- **请求路径**: `/api/dashboard/map/heatmap`
- **请求方法**: `GET`

### 2.6 获取污染类型统计
- **请求路径**: `/api/dashboard/reports/pollution-stats`
- **请求方法**: `GET`

### 2.7 获取任务完成情况统计
- **请求路径**: `/api/dashboard/reports/task-completion-stats`
- **请求方法**: `GET`

### 2.8 获取 AQI 热力图数据
- **请求路径**: `/api/dashboard/map/aqi-heatmap`
- **请求方法**: `GET`

---

## 3. 反馈模块 (Feedback)
**基础路径**: `/api/feedback`

### 3.1 提交反馈 (JSON)
- **功能描述**: 用于测试，使用 JSON 提交反馈。
- **请求路径**: `/api/feedback/submit-json`
- **请求方法**: `POST`
- **所需权限**: 已认证用户
- **请求体**: `FeedbackSubmissionRequest`

### 3.2 提交反馈 (Multipart)
- **功能描述**: 提交反馈，可包含文件。
- **请求路径**: `/api/feedback/submit`
- **请求方法**: `POST`
- **所需权限**: 已认证用户
- **请求体**: `multipart/form-data`
  - `feedback` (part): `FeedbackSubmissionRequest` (JSON)
  - `files` (part, optional): `MultipartFile[]`

### 3.3 获取所有反馈
- **功能描述**: 获取所有反馈（分页）。
- **请求路径**: `/api/feedback`
- **请求方法**: `GET`
- **所需权限**: `ADMIN`
- **查询参数**: `page`, `size`, `sort`

---

## 4. 文件模块 (File)
**基础路径**: `/api/files`
**所需权限**: 公开访问

### 4.1 下载文件
- **请求路径**: `/api/files/download/{fileName}`
- **请求方法**: `GET`
- **路径参数**: `fileName` (string, 必需)

### 4.2 查看文件
- **请求路径**: `/api/files/view/{fileName}`
- **请求方法**: `GET`
- **路径参数**: `fileName` (string, 必需)

---

## 5. 网格模块 (Grid)
**基础路径**: `/api/grids`
**所需权限**: `ADMIN` 或 `DECISION_MAKER`

### 5.1 获取网格列表
- **请求路径**: `/api/grids`
- **请求方法**: `GET`
- **查询参数**:
  - `cityName` (string, 可选)
  - `districtName` (string, 可选)
  - `page`, `size`, `sort`

### 5.2 更新网格信息
- **请求路径**: `/api/grids/{gridId}`
- **请求方法**: `PATCH`
- **所需权限**: `ADMIN`
- **路径参数**: `gridId` (long, 必需)
- **请求体**: `GridUpdateRequest`

---

## 6. 网格员任务模块 (Worker)
**基础路径**: `/api/worker`
**所需权限**: `GRID_WORKER`

### 6.1 获取我的任务
- **请求路径**: `/api/worker`
- **请求方法**: `GET`
- **查询参数**:
  - `status` (enum: `TaskStatus`, 可选)
  - `page`, `size`, `sort`

### 6.2 获取任务详情
- **请求路径**: `/api/worker/{taskId}`
- **请求方法**: `GET`
- **路径参数**: `taskId` (long, 必需)

### 6.3 接受任务
- **请求路径**: `/api/worker/{taskId}/accept`
- **请求方法**: `POST`
- **路径参数**: `taskId` (long, 必需)

### 6.4 提交任务
- **请求路径**: `/api/worker/{taskId}/submit`
- **请求方法**: `POST`
- **路径参数**: `taskId` (long, 必需)
- **请求体**: `TaskSubmissionRequest`

---

## 7. 地图模块 (Map)
**基础路径**: `/api/map`
**所需权限**: 公开访问

### 7.1 获取完整地图网格
- **请求路径**: `/api/map/grid`
- **请求方法**: `GET`

### 7.2 创建或更新地图单元格
- **请求路径**: `/api/map/grid`
- **请求方法**: `POST`
- **请求体**: `MapGrid`

### 7.3 初始化地图
- **请求路径**: `/api/map/initialize`
- **请求方法**: `POST`
- **查询参数**:
  - `width` (int, 可选, 默认 20)
  - `height` (int, 可选, 默认 20)

---

## 8. 路径规划模块 (Pathfinding)
**基础路径**: `/api/pathfinding`
**所需权限**: 公开访问

### 8.1 寻找路径
- **功能描述**: 使用 A* 算法在两点之间寻找最短路径。
- **请求路径**: `/api/pathfinding/find`
- **请求方法**: `POST`
- **请求体**: `PathfindingRequest`
- **示例请求**:
  ```json
  {
    "startX": 0,
    "startY": 0,
    "endX": 19,
    "endY": 19
  }
  ```

---

## 9. 人员管理模块 (Personnel)
**基础路径**: `/api/personnel`
**所需权限**: `ADMIN`

### 9.1 创建用户
- **请求路径**: `/api/personnel/users`
- **请求方法**: `POST`
- **请求体**: `UserCreationRequest`

### 9.2 获取用户列表
- **请求路径**: `/api/personnel/users`
- **请求方法**: `GET`
- **查询参数**:
  - `role` (enum: `Role`, 可选)
  - `name` (string, 可选)
  - `page`, `size`, `sort`

### 9.3 获取单个用户
- **请求路径**: `/api/personnel/users/{userId}`
- **请求方法**: `GET`
- **路径参数**: `userId` (long, 必需)

### 9.4 更新用户信息
- **请求路径**: `/api/personnel/users/{userId}`
- **请求方法**: `PATCH`
- **路径参数**: `userId` (long, 必需)
- **请求体**: `UserUpdateRequest`

### 9.5 更新用户角色
- **请求路径**: `/api/personnel/users/{userId}/role`
- **请求方法**: `PUT`
- **路径参数**: `userId` (long, 必需)
- **请求体**: `UserRoleUpdateRequest`

### 9.6 删除用户
- **请求路径**: `/api/personnel/users/{userId}`
- **请求方法**: `DELETE`
- **路径参数**: `userId` (long, 必需)

---

## 10. 个人资料模块 (Me)
**基础路径**: `/api/me`
**所需权限**: 已认证用户

### 10.1 更新个人资料
- **请求路径**: `/api/me`
- **请求方法**: `PATCH`
- **请求体**: `UserUpdateRequest`

### 10.2 更新我的位置
- **请求路径**: `/api/me/location`
- **请求方法**: `POST`
- **请求体**: `LocationUpdateRequest`

### 10.3 获取我的反馈历史
- **请求路径**: `/api/me/feedback`
- **请求方法**: `GET`
- **查询参数**: `page`, `size`, `sort`

---

## 11. 公共接口模块 (Public)
**基础路径**: `/api/public`
**所需权限**: 公开访问

### 11.1 公众提交反馈
- **请求路径**: `/api/public/feedback`
- **请求方法**: `POST`
- **请求体**: `multipart/form-data`
  - `feedback` (part): `PublicFeedbackRequest` (JSON)
  - `files` (part, optional): `MultipartFile[]`

---

## 12. 主管模块 (Supervisor)
**基础路径**: `/api/supervisor`
**所需权限**: `SUPERVISOR` 或 `ADMIN`

### 12.1 获取待审核的反馈列表
- **请求路径**: `/api/supervisor/reviews`
- **请求方法**: `GET`

### 12.2 批准反馈
- **请求路径**: `/api/supervisor/reviews/{feedbackId}/approve`
- **请求方法**: `POST`
- **路径参数**: `feedbackId` (long, 必需)

### 12.3 拒绝反馈
- **请求路径**: `/api/supervisor/reviews/{feedbackId}/reject`
- **请求方法**: `POST`
- **路径参数**: `feedbackId` (long, 必需)

---

## 13. 任务分配模块 (Tasks)
**基础路径**: `/api/tasks`
**所需权限**: `ADMIN` 或 `SUPERVISOR`

### 13.1 获取未分配的反馈
- **请求路径**: `/api/tasks/unassigned`
- **请求方法**: `GET`

### 13.2 获取可用的网格员
- **请求路径**: `/api/tasks/grid-workers`
- **请求方法**: `GET`

### 13.3 分配任务
- **请求路径**: `/api/tasks/assign`
- **请求方法**: `POST`
- **请求体**:
  ```json
  {
    "feedbackId": 1,
    "assigneeId": 2
  }
  ```

---

## 14. 任务管理模块 (Management)
**基础路径**: `/api/management/tasks`
**所需权限**: `SUPERVISOR`

### 14.1 获取任务列表
- **请求路径**: `/api/management/tasks`
- **请求方法**: `GET`
- **查询参数**:
  - `status` (enum: `TaskStatus`, 可选)
  - `assigneeId` (long, 可选)
  - `severity` (enum: `SeverityLevel`, 可选)
  - `pollutionType` (enum: `PollutionType`, 可选)
  - `startDate` (date, 可选, 格式: YYYY-MM-DD)
  - `endDate` (date, 可选, 格式: YYYY-MM-DD)
  - `page`, `size`, `sort`

### 14.2 分配任务
- **请求路径**: `/api/management/tasks/{taskId}/assign`
- **请求方法**: `POST`
- **路径参数**: `taskId` (long, 必需)
- **请求体**: `TaskAssignmentRequest`

### 14.3 获取任务详情
- **请求路径**: `/api/management/tasks/{taskId}`
- **请求方法**: `GET`
- **路径参数**: `taskId` (long, 必需)

### 14.4 审核任务
- **请求路径**: `/api/management/tasks/{taskId}/review`
- **请求方法**: `POST`
- **路径参数**: `taskId` (long, 必需)
- **请求体**: `TaskApprovalRequest`

### 14.5 取消任务
- **请求路径**: `/api/management/tasks/{taskId}/cancel`
- **请求方法**: `POST`
- **路径参数**: `taskId` (long, 必需)

### 14.6 直接创建任务
- **请求路径**: `/api/management/tasks`
- **请求方法**: `POST`
- **请求体**: `TaskCreationRequest`

### 14.7 获取待处理的反馈
- **请求路径**: `/api/management/tasks/feedback`
- **请求方法**: `GET`
- **所需权限**: `SUPERVISOR` 或 `ADMIN`

### 14.8 从反馈创建任务
- **请求路径**: `/api/management/tasks/feedback/{feedbackId}/create-task`
- **请求方法**: `POST`
- **路径参数**: `feedbackId` (long, 必需)
- **请求体**: `TaskFromFeedbackRequest`