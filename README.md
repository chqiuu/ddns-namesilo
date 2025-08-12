# ddns-namesilo

基于 Java 的 Namesilo 域名服务商 DDNS（动态域名解析）自动更新工具，支持 IPv4 与 IPv6 环境。

## 项目介绍

`ddns-namesilo` 用于自动检测本机公网 IPv4/IPv6 地址，并通过 Namesilo 提供的 API 自动更新域名解析记录，实现家庭宽带或动态 IP 环境下的域名自动指向，无需手动维护。适用于家庭宽带、服务器等公网 IP 经常变动的场景。

## 主要功能

- **自动检测本机公网 IPv4/IPv6**：支持定时（默认 30 分钟）检测本机公网 IPv4 和 IPv6 地址。
- **自动更新 Namesilo 域名解析（A/AAAA 记录）**：如 IP 变更则自动调用 Namesilo API 更新域名 A 记录（IPv4）和 AAAA 记录（IPv6）。
- **多域名支持**：可配置多个需要自动更新的域名。
- **异常处理与日志记录**：详细日志输出，异常场景提示（如未配置域名、获取 IP 失败等）。
- **Web 管理界面**：集成 Swagger 文档，支持参数格式转换、文件上传下载等。
- **多平台浏览器工具**：自动适配 Windows、Mac、Linux 等系统浏览器打开功能。

## 支持的配置项（`application.yml` 核心配置）

```yaml
chqiuu:
  ddns:
    swagger-enable: false  # 是否开启 API 文档（Swagger）
    local-resource-path: ""  # 本地静态资源根路径（可选，默认JAR包相对路径）
    namesiloApiKey: "你的API密钥"  # Namesilo API Key
    domains:                 # 域名列表，每个域名可配置A/AAAA等类型
      - domain: "example.com"
        type: "A"            # 解析类型，A为IPv4，AAAA为IPv6
      - domain: "example.com"
        type: "AAAA"
    excludeLocalIps:         # 排除不需要处理的本地IP（如内网IP），可选
      - "127.0.0.1"
      - "::1"
```
> 详细字段说明参见 `src/main/java/com/chqiuu/ddnsnamesilo/config/DdnsProperties.java`。

## 快速开始

1. 克隆项目代码
   ```bash
   git clone https://github.com/chqiuu/ddns-namesilo.git
   cd ddns-namesilo
   ```
2. 配置 Namesilo API Key 及需自动更新的域名
   - 修改 `application.yml`，填写 `namesiloApiKey` 和 `domains` 列表。
3. 启动项目
   ```bash
   mvn spring-boot:run
   ```
4. 访问 Web 管理界面与 API 文档  
   默认地址为：[http://localhost:8080/swagger/doc.html](http://localhost:8080/swagger/doc.html)

## 依赖说明

- Java 8+
- Spring Boot
- Hutool（工具类库）
- Swagger2（API 文档）
- Maven

## API 说明

项目集成 Swagger2，详细接口文档可通过 `/swagger/doc.html` 访问。支持 DDNS 相关管理、参数配置、日志查看等功能。

## 授权许可

MIT License

## 鸣谢

- [Namesilo](https://www.namesilo.com/) 官方 API
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Hutool](https://hutool.cn/)
