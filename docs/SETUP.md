# 本地开发环境配置

## 1. 环境要求

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17+ | Java 运行环境 |
| Maven | 3.9+ | 项目构建工具 |
| MySQL | 8.0+ | 数据库 |
| Node.js | 18+ | 前端运行环境 |
| npm | 9+ | Node.js 包管理器 |

## 2. 环境检查

### 2.1 检查 Java 版本
```bash
java -version
```

### 2.2 检查 Maven 版本
```bash
mvn -version
```

### 2.3 检查 MySQL
```bash
mysql --version
```

### 2.4 检查 Node.js 和 npm
```bash
node -v
npm -v
```

## 3. MySQL 配置

### 3.1 启动 MySQL
```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql

# Windows (以管理员身份运行)
net start mysql
```

### 3.2 创建数据库
```bash
mysql -u root -p < sql/init.sql
```

### 3.3 配置修改
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
```

## 4. 后端启动

### 4.1 编译项目
```bash
mvn clean compile
```

### 4.2 启动应用
```bash
mvn spring-boot:run
```

或运行主类 `VCashApplication.java`

### 4.3 访问地址
- 后端: http://localhost:8080

## 5. 前端启动

### 5.1 进入前端目录
```bash
cd vcash-web
```

### 5.2 安装依赖
```bash
npm install
```

### 5.3 启动开发服务器
```bash
npm run dev
```

### 5.4 访问地址
- 前端: http://localhost:5173

## 6. 环境变量

### 6.1 Tushare Token（可选）
```bash
# macOS/Linux
export TUSHARE_TOKEN=your_token

# Windows (PowerShell)
$env:TUSHARE_TOKEN="your_token"
```

或在 `application.yml` 中直接配置：
```yaml
tushare:
  token: your_token
```

## 7. 常见问题

### 7.1 端口占用
- 8080: 后端服务
- 5173: 前端开发服务器
- 3306: MySQL

如遇端口冲突，修改对应配置文件或关闭占用进程。

### 7.2 数据库连接失败
1. 确认 MySQL 已启动
2. 检查用户名密码是否正确
3. 确认数据库 `vcash` 已创建
