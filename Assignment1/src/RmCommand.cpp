#include "Commands.h"
#include <iostream>

using namespace std;


RmCommand::RmCommand(string args) : BaseCommand(args) {}

void RmCommand::execute(FileSystem &fs) {
    bool deleted = false;
    Directory *sourceDir = nullptr;
    Directory *working = &fs.getWorkingDirectory();
    if (fs.getRootDirectory().getAbsolutePath() == this->getArgs() || "/" == this->getArgs()) {
        cout << "Can't remove directory" << endl;
        return;
    }
    while (working != nullptr) {
        if (working->getAbsolutePath() == this->getArgs()) {
            cout << "Can't remove directory" << endl;
            return;
        }
        working = working->getParent();
    }

    vector<string> splittedByPath = splitString(this->getArgs(), '/');// splitting source
    string fileName = splittedByPath[splittedByPath.size() - 1];//takes the file name
    int fileNameLength = fileName.size();
    string path = this->getArgs().substr(0, this->getArgs().length() -
                                            (fileNameLength + 1)); //removing filename to have a potential valid path

    if (splittedByPath.size() == 1 && this->getArgs()[0] != '/') {
        sourceDir = &fs.getWorkingDirectory();
    } else {
        sourceDir = isValidPath(path, fs, sourceDir); //got the parent of last
    }

    if (sourceDir == nullptr && (splittedByPath.size() == 1 || fileName == splittedByPath[0])) {
        for (int i = 0; i < signed(fs.getWorkingDirectory().getChildren().size()); i++) {
            if (fs.getWorkingDirectory().getChildren()[i]->getName() == fileName) {
                fs.getWorkingDirectory().removeFile(fileName);
                deleted = true;
                break;
            }
        }
        if (!deleted) {
            cout << "No such file or directory" << endl;
        }
        return;
    }

    if (sourceDir == nullptr) {
        cout << "No such file or directory" << endl;
        return;
    }

    for (int i = 0; i < signed(sourceDir->getChildren().size()); i++) {
        if (sourceDir->getChildren()[i]->getName() == fileName) {
            sourceDir->removeFile(fileName);
            deleted = true;
            break;
        }
    }
    if (!deleted) {
        cout << "No such file or directory" << endl;
    }

}

////returns rm tostring
string RmCommand::toString() {
    return "rm";
}
