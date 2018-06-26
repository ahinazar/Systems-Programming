#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
PwdCommand::PwdCommand(string args) : BaseCommand(args) {}

////executing commend - prints working directory absolute path
void PwdCommand::execute(FileSystem &fs) {
    if (&fs.getWorkingDirectory() == &fs.getRootDirectory()) {
        cout << '/' << endl;
    } else {
        cout << fs.getWorkingDirectory().getAbsolutePath() << endl;
    }
}

////returns pwd toString
string PwdCommand::toString() {
    return "pwd";
}
