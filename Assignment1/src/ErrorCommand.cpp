#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
ErrorCommand::ErrorCommand(string args) : BaseCommand(args) {}

////executing commend - prints error if user input is unknown command
void ErrorCommand::execute(FileSystem &fs) {
    vector<string> splittedBysection = splitString(this->getArgs(), ' ');
    cout << splittedBysection[0] << ": Unknown command" << endl;
}

////returns cd toString
string ErrorCommand::toString() {
    return "error";
}
