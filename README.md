# lempel-ziv-welch-compression

//PLEASE NAVIGATE TO PROJECT's SRC FOLDER AND UTILIZE THE FOLLOWING COMMANDS TO RUN THE PROGRAM

//shortwords.txt (takes less than 10 secs to complete)
java andrew.cmu.edu.abhineec.LZWCompression -c -v "./files/shortwords.txt" "./files/shortwords-compressed.txt"

java andrew.cmu.edu.abhineec.LZWCompression -d -v "./files/shortwords-compressed.txt" "./files/shortwords-decompressed.txt"

//words.html (takes about 1 minute to complete)
java andrew.cmu.edu.abhineec.LZWCompression -c -v "./files/words.html" "./files/words-compressed.html"

java andrew.cmu.edu.abhineec.LZWCompression -d -v "./files/words-compressed.html" "./files/words-decompressed.html"

//CrimeLatLonXY.csv (takes about 1 minute to complete)
java andrew.cmu.edu.abhineec.LZWCompression -c -v "./files/CrimeLatLonXY.csv" "./files/CrimeLatLonXY-compressed.csv"

java andrew.cmu.edu.abhineec.LZWCompression -d -v "./files/CrimeLatLonXY-compressed.csv" "./files/CrimeLatLonXY-decompressed.csv"

//01_Overview.mp4 (takes about 10 minutes to complete)
java andrew.cmu.edu.abhineec.LZWCompression -c -v "./files/01_Overview.mp4" "./files/01_Overview-compressed.mp4"

java andrew.cmu.edu.abhineec.LZWCompression -d -v "./files/01_Overview-compressed.mp4" "./files/01_Overview-decompressed.mp4"


/*Please use the following commands to recompile the project's classes, if need be*/
cd andrew/cmu/edu/abhineec
javac LZWCompression.java HashTable.java Node.java ObjectNode.java SinglyLinkedList.java
cd ../../../..