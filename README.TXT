------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE:
PURPOSE OF PROJECT:
VERSION or DATE:
HOW TO START THIS PROJECT:
AUTHORS:
USER INSTRUCTIONS:

This program will use the following byte encodings for the headers of signals sent. Note that the header will be 
designated as the first element in the byte array sent by the transport layer.
0 - Sys (Request an open channel between client and server. Should be sent by the client only)
1 - Ack (Open a channel. Should be used by the server only)
2 - Request object (Signal sent by the client to obtain a web page or other object from the server)
3 - Code 200
4 - Code 404
5 - Code 304