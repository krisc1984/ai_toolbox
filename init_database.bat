@echo off

echo 正在初始化AI工具箱数据库...
echo 请确保MySQL服务已启动，且root用户密码为123456

:: 连接MySQL并执行schema.sql脚本
mysql -u root -p123456 < "ai-toolbox-backend\src\main\resources\schema.sql"

if %errorlevel% equ 0 (
    echo 数据库初始化成功！
) else (
    echo 数据库初始化失败，请检查MySQL服务状态和密码配置
)

pause
