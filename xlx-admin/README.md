# xlx-admin — 后台管理系统

## 职责

鑫连鑫丝网厂的后台管理系统，供管理员管理产品、分类、工厂图片、证书、询盘和网站配置。

## 技术栈

- Vue 3 + TypeScript
- Vite（开发服务器 + 构建工具）
- Element Plus（UI 组件库）

## 启动

```bash
cd /Volumes/ES4_QT/develop/XLX/xlx-admin

# 首次运行需要安装依赖
npm install

# 启动开发服务器
npm run dev
```

启动后访问：`http://localhost:3001/admin/`

默认管理员账号：`admin` / `admin123`

## 前置条件

后端 `xlx-api` 必须已启动（端口 8080），Vite 会自动将 `/api` 请求代理到后端。

## 功能模块

| 菜单 | 功能 | 说明 |
|------|------|------|
| 仪表盘 | 首页概览 | 系统状态 |
| 产品分类 | 分类管理 | 增删改查、排序 |
| 产品管理 | 产品 CRUD | 产品信息、规格、图片 |
| 工厂图片 | 图片管理 | 工厂展示图片 |
| 证书管理 | 证书管理 | 资质证书、检测报告 |
| 询盘管理 | 询盘处理 | 状态流转、备注 |
| 网站配置 | 配置管理 | 公司信息、联系方式、SEO |

## API 代理配置

`vite.config.ts` 中已配置代理：

```ts
proxy: {
  '/api': { target: 'http://localhost:8080', changeOrigin: true },
  '/uploads': { target: 'http://localhost:8080', changeOrigin: true },
}
```

## 构建

```bash
# 生产构建
npm run build

# 预览构建结果
npm run preview
```

构建产物在 `dist/` 目录，Vite base 为 `/admin/`。

## 验证

```bash
# 1. 启动开发服务器
npm run dev

# 2. 浏览器打开 http://localhost:3001/admin/login

# 3. 用 admin / admin123 登录

# 4. 检查以下功能：
#    - 产品管理：列表是否显示、能否新增/编辑
#    - 分类管理：树形结构是否正确
#    - 询盘管理：状态筛选是否生效
#    - 网站配置：能否修改配置项

# 5. 构建验证
npm run build    # 应无报错
```
