#!/bin/bash

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=sns-service
PROJECT_SUB_NAME=social-server-api

echo "> move spring properties"
cp $REPOSITORY/properties/application.yml $REPOSITORY/zip/src/main/resources
cp $REPOSITORY/properties/application-prod.yml $REPOSITORY/zip/src/main/resources
cp -r $REPOSITORY/properties/.mvn $REPOSITORY/zip

echo "> Build"
cd $REPOSITORY/zip
chmod +x mvnw
./mvnw package

echo "> Build 파일 복사"

cp $REPOSITORY/zip/target/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl sns-service | grep jar | awk '{print $1}')

echo "현재 구동 중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID"]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료되지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> JAR Name에 실행권한 추가"

chmod +x $JAR_NAME

echo "$JAR_NAME 실행"

nohup java -jar -Dspring.profiles.active=prod $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
