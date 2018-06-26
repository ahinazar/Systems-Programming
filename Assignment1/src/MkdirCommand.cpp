#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
MkdirCommand::MkdirCommand(string args) : BaseCommand(args) {}

////executing command - making directory at root specified
void MkdirCommand::execute(FileSystem &fs) {
    Directory *workingDir = nullptr;
    workingDir = isValidPath(this->getArgs(), fs, workingDir); //checking if path is valid - if so, working dir will be null because directory don't exist

    if (workingDir != nullptr) {
        cout << "The directory already exists" << endl;
        return;
    } else {
        workingDir = &fs.getWorkingDirectory();
        int i = 0;
        string argsChange = this->getArgs();

        if (argsChange[0] == '/') { //start from root
            fs.setWorkingDirectory(&fs.getRootDirectory());
            argsChange = argsChange.substr(1, argsChange.length());
        }
        vector<string> splittedPath = splitString(argsChange, '/');

        while (i < signed(splittedPath.size())) { // go by directory names at path
            if (splittedPath[i] == ".." && fs.getWorkingDirectory().getParent() != nullptr) { //go to your parent
                fs.setWorkingDirectory(fs.getWorkingDirectory().getParent());
            } else if (fs.getWorkingDirectory().getChildren().size() == 0) { // make new directory
                fs.getWorkingDirectory().getChildren().push_back(
                        new Directory(splittedPath[i], &fs.getWorkingDirectory()));
                fs.setWorkingDirectory((Directory *) (fs.getWorkingDirectory().getChildren()[0]));
            } else {
                int j;
                for (j = 0; j < signed(fs.getWorkingDirectory().getChildren().size()); j++) {
                    if (splittedPath[i] == fs.getWorkingDirectory().getChildren()[j]->getName() &&
                        fs.getWorkingDirectory().getChildren()[j]->typeOf() == "File") {
                        cout << "The directory already exists" << endl;
                        fs.setWorkingDirectory(workingDir);
                        return;
                    } else if (splittedPath[i] == fs.getWorkingDirectory().getChildren()[j]->getName() &&
                               fs.getWorkingDirectory().getChildren()[j]->typeOf() == "Directory") {
                        fs.setWorkingDirectory((Directory *) fs.getWorkingDirectory().getChildren()[j]);
                        break;
                    } else if (j == signed(fs.getWorkingDirectory().getChildren().size() - 1)) {
                        fs.getWorkingDirectory().getChildren().push_back(
                                new Directory(splittedPath[i], &fs.getWorkingDirectory()));
                        fs.setWorkingDirectory((Directory *) (fs.getWorkingDirectory().getChildren()[
                                fs.getWorkingDirectory().getChildren().size() - 1]));

                    }
                }
            }
            i++;
        }
        fs.setWorkingDirectory(workingDir);
    }

}

////returns mkdir toString
string MkdirCommand::toString() {
    return "mkdir";
}
