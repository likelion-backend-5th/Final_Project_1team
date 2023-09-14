#!/bin/bash

PROJECT_ROOT="/home/ubuntu/app/backend"
APP_LOG="$PROJECT_ROOT/log/application.log"
ERROR_LOG="$PROJECT_ROOT/log/error.log"
DEPLOY_LOG="$PROJECT_ROOT/log/deploy.log"

echo "> 현재 실행 중인 Docker 컨테이너 pid 확인했습니다(최신)." >> "$DEPLOY_LOG"
CURRENT_PID=$(docker container ls -q)

if [ -z $CURRENT_PID ];
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> "$DEPLOY_LOG"
else
  echo "> docker stop $CURRENT_PID" >> "$DEPLOY_LOG" # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  docker stop $CURRENT_PID
  docker rm $CURRENT_PID
  sleep 5
fi


cd /home/ubuntu/app/backend
docker-compose -f docker-back.yml up


