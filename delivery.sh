FILENAME=MEIC-69350-Samuel-Coelho.zip
rm -f $FILENAME
echo "Compiling dissertation"
cd dissertation
make clean
make
cp dissertation.pdf ..
cd ..
echo "Compiling article"
cd article
make clean
make
cp article.pdf ..
cd ..
echo "Generating final zip file $FILENAME"
zip $FILENAME dissertation.pdf article.pdf
rm dissertation.pdf article.pdf
