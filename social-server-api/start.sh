#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=sns-service
PROJECT_SUB_NAME=social-server-api

echo "> move spring properties"
cp $REPOSITORY/properties/application.yml $REPOSITORY/zip/src/main/resources
cp $REPOSITORY/properties/application-prod.yml $REPOSITORY/zip/src/main/resources
cp $REPOSITORY/properties/application-prod1.yml $REPOSITORY/zip/src/main/resources
cp $REPOSITORY/properties/application-prod2.yml $REPOSITORY/zip/src/main/resources
cp -r $REPOSITORY/properties/.mvn $REPOSITORY/zip

echo "> Build"
cd $REPOSITORY/zip
chmod +x mvnw
./mvnw package

echo "> Build 파일 복사"

cp $REPOSITORY/zip/target/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> JAR Name에 실행권한 추가"

chmod +x $JAR_NAME

echo "$JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "$JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
