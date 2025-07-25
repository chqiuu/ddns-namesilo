# ddns-namesilo

基于 Java 实现的 Namesilo 域名服务商 DDNS（动态域名解析）自动更新工具。

## 项目介绍

`ddns-namesilo` 支持自动检测本机公网 IP，并通过 Namesilo 提供的 API 自动更新域名解析记录，实现家宽或动态 IP 环境下的域名自动指向，无需手动维护。适用于家庭宽带、服务器等公网 IP 经常变动的场景。

## 主要功能

- **自动检测本机公网 IP**：定时（默认30分钟）检测本机公网 IP。
- **自动更新 Namesilo 域名解析**：如 IP 变化则自动调用 Namesilo API 更新域名 A 记录。
- **多域名支持**：可配置多个需要自动更新的域名。
- **异常处理与日志记录**：详细日志输出，异常场景提示（如未配置域名、获取 IP 失败等）。
- **Web 管理界面**：集成 Swagger 文档，支持参数格式转换、文件上传下载等。
- **多平台浏览器工具**：自动适配 Windows、Mac、Linux 等系统浏览器打开功能。

## 快速开始

1. **获取 Namesilo API Key**
   - 登录 Namesilo 账号，进入 [API 管理](https://www.namesilo.com/account/api-manager) 获取 API Key。

2. **配置域名信息**
   - 在项目配置文件（如 `application.properties` 或环境变量）中填写：
     ```
     ddnsnamesilo.namesiloApiKey=你的APIKEY
     ddnsnamesilo.domains=你的域名1,你的域名2
     ```

3. **运行项目**
   - 使用 Maven 或 IDE 启动项目。
   - 默认每30分钟自动检测并更新解析记录。

4. **访问管理界面（可选）**
   - 启动后访问 `http://localhost:端口/static/page/index.html` 查看管理界面和相关文档。

## 依赖说明

- Java 8+
- Spring Boot
- Swagger
- dom4j
- 其它依赖详见 `pom.xml`

## API 说明

- 域名解析记录查询：`https://www.namesilo.com/api/dnsListRecords`
- 域名解析记录更新：`https://www.namesilo.com/api/dnsUpdateRecord`
- 具体 API 请求参数和返回见 Namesilo 官方文档。

## 许可证

MIT License

## 鸣谢

感谢 Namesilo 提供开放 API。

---

如需帮助或反馈问题请提交 Issue。
