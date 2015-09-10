DATE=$(date +"%m-%d-%Y %T")
FILENAME="bugreport-$DATE"
RESULTS_PATH="results"
echo "$DATE"
echo "Evalution process..."
echo "Resenting Battery stats"
adb shell dumpsys batterystats --reset
echo "Run the app and press enter to proceed"
read
echo "Disconnect USB cable and press enter"
read
echo "Turn on timer and press enter"
read
echo "Wait until the experiment ends and press enter"
read
echo "Connect USB cable again and press enter"
read
echo "Producing bugreport file"

mkdir $RESULTS_PATH
adb bugreport > $RESULTS_PATH/$FILENAME.txt

echo "Check bugreport file $RESULTS_PATH/$FILENAME"
