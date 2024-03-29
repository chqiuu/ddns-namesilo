package com.chqiuu.ddnsnamesilo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.io.IOException;
import java.net.InetAddress;


@Slf4j
@EnableScheduling
@EnableSwagger2WebMvc
@SpringBootApplication
public class DdnsNamesiloApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext application = SpringApplication.run(DdnsNamesiloApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = null == env.getProperty("server.port") || "80".equals(env.getProperty("server.port")) ? "" : ":" + env.getProperty("server.port");
		String path = null == env.getProperty("server.servlet.context-path") ? "" : env.getProperty("server.servlet.context-path");
		String portPath = port + path;
		String delimiter = String.format("%100s", "").replaceAll("\\s", "=");
		log.info("\n{}\n【{}】项目已启动完成\n访问地址:\n\t" +
						"Local: \t\thttp://localhost{}\n\t" +
						"External: \thttp://{}{}\n\t" +
						"Api: \t\thttp://localhost{}/swagger/doc.html\n{}"
				, delimiter
				, env.getProperty("spring.application.name"), portPath, ip, portPath, portPath
				, delimiter);
	}
}
