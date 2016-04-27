#!/bin/bash
. /etc/profile
ulimit -n 65530
PG_HOME=$(cd `dirname $0`; pwd)
JMX_PORT=12090
JAR_PATH=${PG_HOME}/ddapcenter.jar
CONF_PATH=${PG_HOME}/ddap_config.properties
PIDFILE=$PG_HOME/ddapcenter.pid

selfName=`basename $0`
usage() {
  echo -e "Usage:\n\t$selfName <start|stop|restart|status|update>"
  exit 0
}

checkProcActive() {
  if [ -f $PIDFILE ];then
    PID=`cat $PIDFILE`
    procCount=`ps -ef |grep $PID|grep -v grep |wc -l`
    if [ $procCount -eq 0 ];then
      rm $PIDFILE
    else
      return 0
    fi
  fi

  PID=`ps -ef |grep $JAR_PATH |grep -v grep | awk '{print $2}'`
  if [ -z $PID ];then
    return 1
  else
    echo $PID > $PIDFILE
    return 0
  fi
}

start() {
  echo -n "starting ddapcenter..."
  checkProcActive
  if [ $? -eq 0 ];then
    echo "already running (pid: `cat $PIDFILE`)"
    exit 1
  fi
  
  cd ${PG_HOME}

  $JAVA_HOME/bin/java -server -Xms1g -Xmx1g \
  -Dcom.sun.management.jmxremote.port=${JMX_PORT} \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -jar ${JAR_PATH} \
  -c ${CONF_PATH} 1>/dev/null 2>&1 &

  echo $! > ${PIDFILE}

  checkProcActive
  if [ $? -eq 0 ];then
    echo "success"
  else
    echo "failed"
  fi
}

stop() {
  echo -n "stopping ddapcenter..."
  while :
  do
    checkProcActive
    if [ $? -eq 0 ];then
      kill -TERM `cat $PIDFILE` 2>/dev/null
    else
      echo "success"
      return 0
    fi
    sleep 2
  done
}

status() {
  checkProcActive
  if [ $? -eq 0 ];then
    echo "ddapcenter (pid: `cat $PIDFILE`) is running"
  else
    echo "ddapcenter was stopped"
  fi
}

update() {
  cd $PG_HOME
  rsync -av 172.16.49.4::ddap-center ./
}

case $1 in
  start)
    start
  ;;
  stop)
    stop
  ;;
  restart)
    stop
    start
  ;;
  status)
    status
  ;;
  update)
    update
  ;;
  *)
    usage
  ;;
esac
