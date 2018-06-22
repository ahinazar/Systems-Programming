//
// Created by ahinazar@wincs.cs.bgu.ac.il on 1/6/18.
//

#include <stdlib.h>
#include <iostream>
#include "../include/connectionHandler.h"
#include "../include/Task.h"
#include <boost/thread.hpp>
#include <boost/algorithm/string.hpp>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);

    if (!connectionHandler.connect()) {
        return 1;
    }

    bool done = false;

    Task writeTask(connectionHandler,done);
    boost::thread writingThread( writeTask );

    while (true) {
        std::string answer;
        if(!connectionHandler.getLine(answer))
            break;

        unsigned int len=answer.length();
        answer.resize(len-1);
        std::cout << answer << std::endl;
        if ( boost::iequals(answer,"ACK signout succeeded") ) {
            done = true;
            break;
        }
    }

    std::cout << "Ready to exit. Press enter" << std::endl;
    writingThread.join();

    return 0;
}
