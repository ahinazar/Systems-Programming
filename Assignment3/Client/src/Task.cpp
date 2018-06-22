//
// Created by ahinazar@wincs.cs.bgu.ac.il on 1/6/18.
//

#include "../include/Task.h"

Task::Task(ConnectionHandler& connectionHandler, bool& done):connectionHandler(connectionHandler), done(done) {};

void Task::operator()() {
    while (true) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        if ( done || !connectionHandler.sendLine(line) )
            break;
    }
}
