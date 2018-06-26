#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
MkfileCommand::MkfileCommand(string args) : BaseCommand(args) {}

////executing command - making file at path specified
void MkfileCommand::execute(FileSystem &fs) {
    Directory *workingDir = nullptr;
    Directory *workingDirSaved = nullptr;
    vector<string> splittedBySection = splitString(this->getArgs(), ' ');
    workingDirSaved = &fs.getWorkingDirectory(); //for use if i'm not changing files

    if (splittedBySection.size() != 2) { //if there is no size or path
        return;
    }

    vector<string> splittedByPath = splitString(splittedBySection[0], '/');// there is spaces between flag -s and path
    string fileName = splittedByPath[splittedByPath.size() - 1];//takes the file name

    int fileNameLegth = fileName.size();
    int sizeOfFile = stoi(splittedBySection[1]);


    string path = splittedBySection[0].substr(0, splittedBySection[0].length() - (fileNameLegth +
                                                                                  1)); //removing filename to have a potential valid path


    workingDir = isValidPath(path, fs, workingDir); //checks if there is valid path
    if (splittedByPath.size() == 1 && workingDir != nullptr) {
        cout << "The file already exists" << endl;
        return;
    } else if (splittedByPath.size() == 1 && workingDir == nullptr) { //checking if file exist
        for (int j = 0; j < signed(fs.getWorkingDirectory().getChildren().size()); j++) {
            if (fs.getWorkingDirectory().getChildren()[j]->getName() == fileName) {
                cout << "File already exists" << endl;
                return;
            }
        }
        fs.getWorkingDirectory().addFile(new File(fileName, sizeOfFile));
        fs.setWorkingDirectory(workingDirSaved);
        return;
    }

    if (workingDir == nullptr) { // no such path
        cout << "The system cannot find the path specified" << endl;
        return;
    } else { // file already exist or adding new file
        for (int j = 0; j < signed(workingDir->getChildren().size()); j++) {
            if (workingDir->getChildren()[j]->getName() == fileName) {
                cout << "File already exists" << endl;
                return;
            }
        }
        fs.setWorkingDirectory(workingDir);
        fs.getWorkingDirectory().addFile(new File(fileName, sizeOfFile));
    }
    fs.setWorkingDirectory(workingDirSaved); //return to working directory anyway
}

////returns mkfile toString
string MkfileCommand::toString() {
    return "mkfile";
}
