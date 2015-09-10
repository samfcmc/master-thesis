BATTERY_HISTORIAN_PATH="$GOPATH/src/github.com/google/battery-historian"
EXEC="$BATTERY_HISTORIAN_PATH/cmd/battery-historian/battery-historian.go"
echo "Running Battery historian..."
cd $BATTERY_HISTORIAN_PATH
bash setup.sh
go run $EXEC
