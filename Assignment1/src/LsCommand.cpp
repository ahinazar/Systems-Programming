#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
LsCommand::LsCommand(string args) : BaseCommand(args) {}

////executing command - prints directory context
void LsCommand::execute(FileSystem &fs) {

    Directory *workingDir = nullptr;
    string argsChange = this->getArgs(); // will change - so i assign string for it
    vector<string> splittedBySection = splitString(argsChange, ' ');// there is spaces between flag -s and path

    if (splittedBySection.size() == 0 || (splittedBySection.size() == 1 && splittedBySection[0] == "-s")) { // not spaces or there is space and we want to sort printing by size
        workingDir = &fs.getWorkingDirectory();
    } else if (splittedBySection[splittedBySection.size() - 1] != "/") { //not start from root
        workingDir = isValidPath(splittedBySection[splittedBySection.size() - 1], fs, workingDir);
    } else { //start from root
        workingDir = &fs.getRootDirectory();
    }

    if (workingDir == nullptr) { //there is no such path
        cout << "The system cannot find the path specified" << endl;
        return;
    }

    workingDir->sortByName();

    if (splittedBySection.size() > 1 || (splittedBySection.size() == 1 && splittedBySection[0] == "-s")) { // there is -s and path, than sort it by size
        workingDir->sortBySize();
    }

    for (int i = 0; i < signed(workingDir->getChildren().size()); i++) { //print
        if (workingDir->getChildren()[i] != nullptr) {
            if (workingDir->getChildren()[i]->typeOf() ==
                "Directory") {
                cout << "DIR";
            } else {
                cout << "FILE";
            }
            cout << "\t" << workingDir->getChildren()[i]->getName() << "\t" << workingDir->getChildren()[i]->getSize()
                 << endl;

        }
    }
}

////returns ls toString
string LsCommand::toString() {
    return "ls";
}
