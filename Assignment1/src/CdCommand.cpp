#include "Commands.h"
#include <iostream>

using namespace std;

////Constractor
CdCommand::CdCommand(string args) : BaseCommand(args) {}

////executing commend - change current working directory to args directory if it can and it's valid
void CdCommand::execute(FileSystem &fs) {
    Directory *workingDir = &fs.getWorkingDirectory();
    string argsChange = this->getArgs();

    if (argsChange[0] == '/') { //start from root
        fs.setWorkingDirectory(&fs.getRootDirectory());
        argsChange = argsChange.substr(1, argsChange.length());
    }

    int i = 0;
    vector<string> splittedPath = splitString(argsChange, '/'); //split path by slash ('/'


    while (i < signed(splittedPath.size())) { // running on path's directory names
        if (splittedPath[i] == ".." && fs.getWorkingDirectory().getParent() != nullptr) { //go to your parent
            fs.setWorkingDirectory(fs.getWorkingDirectory().getParent());
        } else {
            if (fs.getWorkingDirectory().getChildren().size() == 0) { //we didn't finish and there are not children to continue - don't change working directory
                cout << "The system cannot find the path specified" << endl;
                fs.setWorkingDirectory(workingDir);
                return;
            }
            int j;
            for (j = 0; j < signed(fs.getWorkingDirectory().getChildren().size()); j++) { // run on children's children and check if it's directory - if it fits - continue to this directory
                if (splittedPath[i] == fs.getWorkingDirectory().getChildren()[j]->getName() &&
                    fs.getWorkingDirectory().getChildren()[j]->typeOf() == "Directory") {
                    fs.setWorkingDirectory((Directory *) fs.getWorkingDirectory().getChildren()[j]);
                    break;
                } else if (j == signed(fs.getWorkingDirectory().getChildren().size() - 1)) { //path was'nt found, don't change anything
                    cout << "The system cannot find the path specified" << endl;
                    fs.setWorkingDirectory(workingDir);
                    return;
                }
            }
        }
        i++;
    }

}

////returns cd tostring
string CdCommand::toString() {
    return "cd";
}
