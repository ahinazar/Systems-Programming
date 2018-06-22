#ifndef CLIENT_TASK_H
#define CLIENT_TASK_H

#include <boost/thread/mutex.hpp>
#include "connectionHandler.h"

class Task {
private:
    ConnectionHandler& connectionHandler;
    bool& done;

public:
    Task(ConnectionHandler& connectionHandler, bool& done);
    void operator()();
};


#endif //CLIENT_TASK_H
