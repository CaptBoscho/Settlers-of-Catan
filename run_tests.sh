#!/bin/sh

ant server > /dev/null &
#echo $! > /tmp/server_process.pid
ant test
kill -9 `lsof -i:8081 -t`

echo "finished"

