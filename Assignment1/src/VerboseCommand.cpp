#include "Commands.h"
#include "GlobalVariables.h"
#include <iostream>

using namespace std;

////Constructor
VerboseCommand::VerboseCommand(string args) : BaseCommand(args) {}

////executing command - changes verbose
void VerboseCommand::execute(FileSystem &fs) {
    if (stoi(this->getArgs()) != 0 && stoi(this->getArgs()) != 1 && stoi(this->getArgs()) != 2 &&
        stoi(this->getArgs()) != 3) {
        cout << "Wrong verbose input" << endl;
        return;
    }
    if (stoi(this->getArgs()) == 0) {
        verbose = 0;
        return;
    } else if (stoi(this->getArgs()) == 1) {
        verbose = 1;
    } else if (stoi(this->getArgs()) == 2) {
        verbose = 2;
    } else if (stoi(this->getArgs()) == 3) {
        verbose = 3;
    }
}

////returns verbose toString
string VerboseCommand::toString() {
    return "verbose";
}
