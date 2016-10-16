# Chat

This repository contains two classes that create a multithreaded chat programm which lets the user send and reveive messages through a GUI. 

The first class is the chat server which creates a client handler object for each client that makes a connection. Every client handler object has a separated thread that reads incoming messages from a certain client. Once a message is read by one of the client handlers it gets distributed to all the other participants in the chat.

The other class is the client class which makes a connection which the server class and and shows the GUI where the user sends and receives messages. Any number of clients can join the same chat with using the server class.
