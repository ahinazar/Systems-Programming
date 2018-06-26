#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
ExecCommand::ExecCommand(string args, const vector<BaseCommand *> &history) : BaseCommand(args), history(history) {}

////executing command - change current working directory to args directory if it can and it's valid
void ExecCommand::execute(FileSystem &fs) {
    if (stoi(this->getArgs()) >= signed(history.size()) || stoi(this->getArgs()) < 0) {
        cout << "Command not found" << endl;
        return;
    }
    history[stoi(this->getArgs())]->execute(fs);
}

////returns exec toString
string ExecCommand::toString() {
    return "exec";
}
