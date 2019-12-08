# Fixed list:
* Fixes required:
    * Lacking error management: I removed all throw exception and handel everything at the place
    * Incorrect buffer management in the TCP client and server: I make sure that it work now by set the buffer size to 3 (server) and useing telnet to test.

* Random notes:
    * why the start() method? hmm if i dont to .start() then my server cant handle multiple clients.ss
    * why userid, why not use thread id? OH I didnt think about that ! fixed !!
    * in while (run), when will run be false? Also, global variable in loop condition is generally bad!: fixed, there are one part that i use While(true) when i want to loop to run forever
    * in input.read(super.buffer);, why super? sry I was stupid.
    * timing logic does not work according to instructions: removed trim.

# Introduction
All .java files are in /src. I also include all screenshots in /pic in case the screenshots in report are hard to see.
# Requirements
JDK 11
# HOW to Run
1. open the terminal
2. ```cd src```
3. ```javac *java```
## Client
1. ```javac UDPClient.java``` to complie to program. 
2. ```java -cp UDPClient ServerIPaddress ServerPort BufferSize transferRate```
3. press ctrl + c to terminate the program.
NOTE: TCPClinet's have the same input's sturcture
## Server
3. ```javac UDPServer.java``` to complie to program. 
4. ```java -cp UDPServer```
5. press ctrl + c to terminate the program.
NOTE: to run TCPserver Do the same process as runing UDPServer

