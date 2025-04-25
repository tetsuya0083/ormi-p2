#!/bin/bash

echo "current user: $(whoami)"
echo "current home directory: $HOME"

if [ "$(whoami)" = "root" ]; then
  sudo -u ec2-user bash -c "/home/ec2-user/ormi-p2/deploy.sh"
  exit 0
fi

cd /home/ec2-user/ormi-p2 || exit 1

# need for deploy.sh
echo "git pull..."
git pull origin develop

aws s3 cp "s3://$S3_BUCKET/app.jar" "build/libs/app.jar" || exit 1
JAR_PATH="build/libs/app.jar"

if [ ! -f "$JAR_PATH" ]; then
  echo "JAR file not found at $JAR_PATH"
  exit 1
fi

chmod 755 "$JAR_PATH"
chown ec2-user:ec2-user "$JAR_PATH"

PID=$(pgrep -u ec2-user -f "java -jar $JAR_PATH")
if [ -n "$PID" ]; then
  echo "Stopping existing app (PID: $PID)..."
  kill "$PID"
  sleep 3
fi

LOG_DIR="/home/ec2-user/weblog"
LOG_FILE="$LOG_DIR/$(date '+%Y%m%d_%H%M').log"

echo "Starting app..."
nohup java -jar "$JAR_PATH" > "$LOG_FILE" 2>&1 &
sleep 10 # 임시 코드, todo 추후 response loop check 방식으로 개선할 예정

RES_CHK_URL="http://localhost:8080"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" $RES_CHK_URL)

if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 404 ]; then
  MESSAGE="✅ [Deploy Success!] jar: $JAR_PATH log: $LOG_FILE resp: $HTTP_CODE"
  echo "Deploy Success!"
else
  MESSAGE="❌ [Deploy Failed!] jar: $JAR_PATH log: $LOG_FILE resp: $HTTP_CODE"
  echo "Deploy Failed!"
fi

JSON="{\"content\": \"$MESSAGE\"}"
echo "WebHook URL:'$DISCORD_WEBHOOK_URL'"
echo "JSON: $JSON"
# curl -H "Content-Type: application/json" -X POST -d "$JSON" "$DISCORD_WEBHOOK_URL"
curl -X POST \
     "$DISCORD_WEBHOOK_URL" \
     -H "Content-Type: application/json" \
     --data "$JSON"
# -H = --header, -X = --request -d = --data
