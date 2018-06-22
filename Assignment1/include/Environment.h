#ifndef ENVIRONMENT_H_
#define ENVIRONMENT_H_

#include "Files.h"
#include "Commands.h"
#include <string>
#include <vector>

using namespace std;

class Environment {
private:
    vector<BaseCommand *> commandsHistory;
    FileSystem fs;

public:
    Environment(Environment &&copy);

    Environment(const Environment &toCopyFrom);

    virtual ~Environment();

    virtual Environment &operator=(const Environment &toAssign);

    Environment &operator=(Environment &&toAssign);

    void clean();

    void copy(const Environment &dir);

    void steal(Environment &dir);

    vector<string> splitString(string args, string splitBy);

    Environment();

    void start();

    FileSystem &getFileSystem(); // Get a reference to the file system
    void addToHistory(BaseCommand *command); // Add a new command to the history
    const vector<BaseCommand *> &getHistory() const; // Return a reference to the history of commands
};

#endif
