# 使用官方推荐的镜像
FROM openjdk:17

# 设置工作目录（遵循Linux文件系统层次标准）
WORKDIR /data/app

# 复制构建好的Spring Boot应用
COPY rbc-1.0.0.jar rbc.jar

# 设置JVM参数（支持通过环境变量覆盖）
ENV JAVA_OPTS="-Xms512m -Xmx512m -Dfile.encoding=UTF-8"

# 安全上下文配置（非root用户运行）
RUN groupadd -r springuser && useradd -r -g springuser springuser && chown -R springuser:springuser /data/app
USER springuser

# 容器启动命令（支持环境变量覆盖）
ENTRYPOINT exec java $JAVA_OPTS -jar rbc.jar


# 声明暴露端口（与application.properties中的server.port保持一致）
EXPOSE 8080