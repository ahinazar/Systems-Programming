#include "Commands.h"
#include <iostream>

using namespace std;

//// Constuctor
MvCommand::MvCommand(string args) : BaseCommand(args) {}

////executing commend - moving directory or file to specific directory if it can and it's valid
void MvCommand::execute(FileSystem &fs) {
    Directory *destDir = nullptr;
    Directory *working = &fs.getWorkingDirectory();

    vector<string> splittedBySection = splitString(this->getArgs(), ' '); //gets source+ destination;
    vector<string> splittedByPath = splitString(splittedBySection[0], '/');// there is spaces between flag -s and path
    string fileName = splittedByPath[splittedByPath.size() - 1];//takes the file name
    int fileNameLength = fileName.size();
    string path = splittedBySection[0].substr(0, splittedBySection[0].length() - (fileNameLength +
                                                                                  1)); //removing filename to have a potential valid path

    if (path.size() == 0 && splittedBySection[0][0] == '/') {
        path = "/";
    }

    Directory *sourceDir = nullptr;
    destDir = isValidPath(splittedBySection[1], fs, destDir);


    if (splittedByPath.size() == 1 && splittedBySection[0][0] != '/') {
        sourceDir = &fs.getWorkingDirectory();
    } else {
        sourceDir = isValidPath(path, fs, sourceDir); //got the parent of last
    }
    if (path == splittedBySection[1] ||
        splittedBySection.size() != 2) { //if i want to move son to his own father or something wrong
        return;
    } else if (splittedByPath.size() == 1 && fileName == "..") { //parent of working
        cout << "Can't move directory" << endl;
        return;
    } else if (sourceDir == nullptr || destDir == nullptr) { //something is invalid
        cout << "No such file or directory" << endl;
        return;
    } else if (splittedBySection[0] == "/") { //moving root
        cout << "Can't move directory" << endl;
        return;

    } else if (fileName == "..") {
        if (sourceDir->getParent() == nullptr) {
            cout << "No such file or directory" << endl;
            return;
        } else if (sourceDir->getParent()->getAbsolutePath() == fs.getRootDirectory().getAbsolutePath()) {
            cout << "Can't move directory" << endl;
            return;
        }
        sourceDir = sourceDir->getParent();
        fileName = sourceDir->getName();
    }

    if (fileName != "..") {
        if (sourceDir->getChildren().size() == 0) {
            cout << "No such file or directory" << endl;
            return;
        }
        bool isFile = false;
        for (int i = 0;
             i < signed(destDir->getChildren().size()); i++) { //check if there is file in destDir with the same name
            if (fileName == destDir->getChildren()[i]->getName()) {
                return;
            }
        }
        bool found = false;
        for (int i = 0; i < signed(sourceDir->getChildren().size()); i++) { //finding the file if its directory
            if (fileName == sourceDir->getChildren()[i]->getName() &&
                sourceDir->getChildren()[i]->typeOf() == "Directory") {
                sourceDir = (Directory *) sourceDir->getChildren()[i];
                found = true;
                if (sourceDir->getAbsolutePath() ==
                    fs.getWorkingDirectory().getAbsolutePath()) { //if its working directory
                    cout << "Can't move directory" << endl;
                    return;
                }
                while (working != nullptr) {                //if its some parent of working directory
                    if (working->getAbsolutePath() == sourceDir->getAbsolutePath()) {
                        cout << "Can't move directory" << endl;
                        return;
                    }
                    working = working->getParent();
                }
                break;
            } else if (fileName == sourceDir->getChildren()[i]->getName() &&
                       sourceDir->getChildren()[i]->typeOf() == "File") {
                isFile = true;
                found = true;
                break;
            }
        }
        if (!found) {
            cout << "No such file or directory" << endl;
            return;
        }

        if (!isFile) { //if its file -> file cant be parent - no need to check
            Directory *prevParentDest = destDir;
            while (prevParentDest != nullptr) { //if i want to move a parent to his son
                if (prevParentDest->getAbsolutePath() == sourceDir->getAbsolutePath()) {
                    return;
                }
                prevParentDest = prevParentDest->getParent();
            }
        }

    }


    CpCommand *cp = new CpCommand(this->getArgs());
    cp->
            execute(fs);

    if (destDir != nullptr)
        for (int i = 0; i < signed(destDir->getChildren().size()); i++) {
            if (destDir->getChildren()[i]->getName() == fileName) { //Requested file was copied!!!
                RmCommand *rm = new RmCommand(splittedBySection[0]); // REMOVING ONLY THE SOURCE AFTER COPIED
                rm->execute(fs);
                delete (rm);
            }
        }
    delete (cp);
}

////returns mv toString
string MvCommand::toString() {
    return "mv";
}
