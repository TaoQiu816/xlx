# xlx-web — 前台展示网站

## 职责

鑫连鑫丝网厂的前台展示网站，面向客户展示产品、工厂、证书等信息，支持在线询盘。

## 技术栈

- Vue 3 + TypeScript
- Vite（开发服务器 + 构建工具）
- Tailwind CSS（样式框架）

## 启动

```bash
cd /Volumes/ES4_QT/develop/XLX/xlx-web

# 首次运行需要安装依赖
npm install

# 启动开发服务器
npm run dev
```

启动后访问：`http://localhost:3000`

## 前置条件

后端 `xlx-api` 必须已启动（端口 8080），Vite 会自动将 `/api` 请求代理到后端。

## 页面路由

| 路径 | 页面 | 说明 |
|------|------|------|
| `/` | 首页 | Banner、推荐产品、工厂预览、证书预览 |
| `/products` | 产品列表 | 分类筛选 + 产品网格 + 分页 |
| `/products/:slug` | 产品详情 | 图片画廊、规格表、询盘表单 |
| `/about` | 关于我们 | 公司介绍、核心数字 |
| `/factory` | 工厂展示 | 工厂图片灯箱 |
| `/certificates` | 资质证书 | 证书卡片 + 灯箱 |
| `/faq` | 常见问题 | 分组折叠面板 |
| `/contact` | 联系我们 | 联系信息 + 询盘表单 + 文件上传 |

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

构建产物在 `dist/` 目录。

## 验证

```bash
# 1. 启动开发服务器
npm run dev

# 2. 浏览器打开 http://localhost:3000

# 3. 检查以下页面：
#    - 首页：Banner 和推荐产品是否显示
#    - /products：产品列表是否加载
#    - /contact：询盘表单是否可提交

# 4. 构建验证
npm run build    # 应无报错
```
