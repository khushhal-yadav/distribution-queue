# Distribution-queues

Requirements: java 8, maven

There is always a scope of improvement!

All the implementations are currently exposed by ConcurrentLinkedList for handshake to have a safe-gaurd against concurrent environment. Alternatively, ArrayDeque can be used, which is faster than List implementation when used as a queue. 

Approach is very simple: having a Broker which is used by RandomNumberGenerator to send 'ints' and PrimeNumberChecker to respond back with the result.

It contains three implementations of distributed queue(Broker).

First two implementations are based on shared access when both the applications are on same server.
<br/>Third implementation opens a socket and let the PrimeNumberChecker running anywhere to work on the 'ints' published by RandomNumberGenerator  

<b>SharedQueue</b> interface defines the contract for distributed queues which leverage shared access and the two implementations are:

<b><i>Fastest</i>: SharedJvmMemoryBasedQueue</b> <- When both the application are running under the same JVM and have access to common JVM memory/heap allocated.

<b>SharedFileBasedQueue</b> <- Supports applications running on the same server, in same/different JVMs. As of now, communication is via Media of 'Shared file', can be modified to leverage on any other shared Media like memory map file. Its performance depends on the Media involved and the corresponding I/O related to that.
<br/><i>start()</i> method starts the queue/broker.

<b>SocketBasedQueue</b> <- Opens a socket and let different PrimeNumberCheck applications running in same JVM/ other JVM on the same server or even in JVM on other server, works on the input and write back the Result.

<br> 

<b><i>TestSharedFileBasedQueue</i></b> starts the independent SharedFileBasedQueue and passes it to <i>RandomNumberGenerator</i> and <i>PrimeNumberChecker</i> to communicate with each other.
<br><b><i>TestSharedJvmMemoryBasedQueue</i></b> passes reference of SharedJvmMemoryBasedQueue to <i>RandomNumberGenerator</i> and <i>PrimeNumberChecker</i> to communicate with each other.
<br><b><i>TestSocketBasedQueue</i></b> opens up the socket from <i>RandomNumberGenerator</i> and <i>PrimeNumberChecker</i> interact via that to <i>RandomNumberGenerator</i>.


  