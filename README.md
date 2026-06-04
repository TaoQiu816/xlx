# 丝网工厂官网与销售系统

安平县鑫联信金属丝网厂 B2B 官网 + 产品展示 + 后台管理 + 询盘系统。

## 项目结构

```text
xlx-api/      Spring Boot 后端
xlx-web/      Vue 3 前台展示
xlx-admin/    Vue 3 后台管理
uploads/      上传文件
docs/         文档
```

## 快速启动

### 数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE xlx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### 后端

```bash
cd xlx-api
# 配置数据库连接（修改 src/main/resources/application.yml）
mvn spring-boot:run
```

### 前台

```bash
cd xlx-web
npm install
npm run dev
```

### 后台

```bash
cd xlx-admin
npm install
npm run dev
```

## 技术栈

- 后端：Spring Boot 3.x + MyBatis Plus + Java 17 + MySQL
- 前台：Vue 3 + Vite + Tailwind CSS
- 后台：Vue 3 + Vite + Element Plus
