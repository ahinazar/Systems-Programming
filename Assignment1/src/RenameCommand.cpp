#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
RenameCommand::RenameCommand(string args) : BaseCommand(args) {}

////executing commend - renaming directory or file at specific directory if it can and it's valid
void RenameCommand::execute(FileSystem &fs) {
    Directory *sourceDir = nullptr;
    vector<string> splittedBySection = splitString(this->getArgs(), ' '); //gets path+size;

    if (splittedBySection.size() != 2) { //if there is no source or destination
        return;
    }
    if (splittedBySection[0] == "/") {
        return;
    }

    //#############sparates the last File/Dir from Path###################
    vector<string> splittedByPath = splitString(splittedBySection[0], '/');// there is spaces between flag -s and path
    string fileName = splittedByPath[splittedByPath.size() - 1];//takes the file name
    int fileNameLegth = fileName.size();
    string path = splittedBySection[0].substr(0, splittedBySection[0].length() - (fileNameLegth +
                                                                                  1)); //removing filename to have a potential valid path
    bool exists = false;
    //####################################################################
    if (splittedByPath.size() == 1 && path[0] != '/') {
        sourceDir = &fs.getWorkingDirectory();
        if (splittedBySection[0] == fs.getWorkingDirectory().getAbsolutePath() ||
            splittedBySection[0] == fs.getWorkingDirectory().getName()) {
            cout << "Can't rename the working directory" << endl;
            return;
        } else if (fileName == "..") {
            sourceDir->getParent()->setName(splittedBySection[1]);
            return;
        }
        for (int j = 0; j < signed(sourceDir->getChildren().size()); j++) { //there is file with such a name
            if (splittedBySection[1] == sourceDir->getChildren()[j]->getName()) {
                return;
            }
        }
        for (int j = 0; j < signed(sourceDir->getChildren().size()); j++) { //searching fot the file/directory to copy
            if (sourceDir->getChildren()[j]->getName() == fileName) {
                sourceDir->getChildren()[j]->setName(splittedBySection[1]);
                exists = true;
                break;
            }
        }
        if (!exists) {
            cout << "No such file or directory" << endl;
        }
        return;
    }

    sourceDir = isValidPath(path, fs, sourceDir); //got the parent of last

    if (sourceDir == nullptr) {
        return;
    } else if (fileName == "..") {
        sourceDir = sourceDir->getParent();
        if (sourceDir->getAbsolutePath() == fs.getWorkingDirectory().getAbsolutePath()) {
            cout << "Can't rename the working directory" << endl;
        } else {
            sourceDir->setName(splittedBySection[1]);
        }
        return;
    } else if (splittedBySection[0] == fs.getWorkingDirectory().getAbsolutePath() ||
               splittedBySection[0] == fs.getWorkingDirectory().getName()) {
        cout << "Can't rename the working directory" << endl;
        return;
    } else {
        for (int j = 0; j < signed(sourceDir->getChildren().size()); j++) { //there is file with such a name
            if (splittedBySection[1] == sourceDir->getChildren()[j]->getName()) {
                return;
            }
        }
        for (int j = 0; j < signed(sourceDir->getChildren().size()); j++) { //searching fot the file/directory to copy
            if (sourceDir->getChildren()[j]->typeOf() == "Directory") {
                if (((Directory *) sourceDir->getChildren()[j])->getAbsolutePath() ==
                    fs.getWorkingDirectory().getAbsolutePath()) {
                    cout << "Can't rename the working directory" << endl;
                    return;
                }
            } else if (sourceDir->getChildren()[j]->getName() == fileName) {
                sourceDir->getChildren()[j]->setName(splittedBySection[1]);
                exists = true;
                break;
            }
        }
        if (!exists) {
            cout << "No such file or directory" << endl;
        }
    }
}

////returns rename toString
string RenameCommand::toString() {
    return "rename";
}
