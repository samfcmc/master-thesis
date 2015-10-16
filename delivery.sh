FILENAME=MEIC-69350-Samuel-Coelho.zip
echo "Compiling dissertation"
cd dissertation
make clean
make
cd ..
echo "Compiling article"
cd article
make clean
make
cd ..
echo "Generating final zip file $FILENAME"
zip $FILENAME dissertation/dissertation.pdf article/article.pdf
