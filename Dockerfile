# 构建阶段（官方OpenJDK镜像）
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /rbc
COPY . .
RUN ./mvnw clean package -DskipTests

# 运行阶段（官方JRE镜像）
FROM eclipse-temurin:17-jre-jammy
WORKDIR /rbc

# 创建非root用户
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser
USER javauser

# 复制构建产物
COPY --from=builder --chown=javauser:javauser /rbc/target/*.jar rbc.jar

# 动态配置入口
ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV DB_URL="" DB_USER="" DB_PASSWORD="" REDIS_HOST=""

# 启动命令
ENTRYPOINT exec java $JAVA_OPTS -jar rbc.jar \
  --spring.datasource.url=${DB_URL} \
  --spring.datasource.username=${DB_USER} \
  --spring.datasource.password=${DB_PASSWORD} \
  --spring.redis.host=${REDIS_HOST}

EXPOSE 8080