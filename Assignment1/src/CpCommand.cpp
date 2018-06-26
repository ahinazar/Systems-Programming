#include "Commands.h"
#include <iostream>

using namespace std;

//// Constructor
CpCommand::CpCommand(string args) : BaseCommand(args) {}

////executing commend - copying  directory or file to specific directory if it can and it's valid
void CpCommand::execute(FileSystem &fs) {
    Directory *destDir = nullptr;
    Directory *sourceDir = nullptr;
    vector<string> splittedBySection = splitString(this->getArgs(), ' '); //splitting path's for source and destination

    if (splittedBySection.size() != 2) { //if there is no source or destination
        return;
    }

    //#############sparates the last File/Dir from Path###################
    vector<string> splittedByPath = splitString(splittedBySection[0], '/');// splitting source by slash ('/')
    string fileName = splittedByPath[splittedByPath.size() - 1];//takes the file name
    int fileNameLength = fileName.size();
    string path = splittedBySection[0].substr(0, splittedBySection[0].length() - (fileNameLength +
                                                                                  1)); //removing filename to have a potential valid path

    if (path == splittedBySection[1]) { //it's the same
        return;
    }

    BaseFile *sourceToCopy = nullptr;
    //####################################################################

    if (splittedBySection[1] != "/") { //don't start from root
        destDir = isValidPath(splittedBySection[1], fs, destDir);
    } else { //start from root
        destDir = &fs.getRootDirectory();
    }
    if (path != "/") { //don't start from root
        if (splittedBySection[0] == fileName) {
            sourceDir = &fs.getWorkingDirectory();
        } else { //got the parent of last
            sourceDir = isValidPath(path, fs, sourceDir); //check if it's valid
        }
    } else { //start from root
        sourceDir = &fs.getRootDirectory();
    }

    if (!destDir || !sourceDir) { //paths are not valid
        cout << "No such file or directory" << endl;
        return;
    }

    for (int j = 0; j < signed(destDir->getChildren().size()); j++) { //searching for the file/directory to copy
        if (destDir->getChildren()[j]->getName() == fileName) { //destination is file - don't do anything
            return;
        }
    }

    for (int j = 0; j < signed(sourceDir->getChildren().size()); j++) { //searching fot the file/directory to copy
        if (sourceDir->getChildren()[j]->getName() == fileName) {
            sourceToCopy = sourceDir->getChildren()[j];
            break;
        }
    }

    if (!sourceToCopy) { //file/directory doesnt exist
        cout << "No such file or directory" << endl;
        return;
    }

    if (sourceToCopy->typeOf() == "File") { //add it
        destDir->addFile(new File(sourceToCopy->getName(), sourceToCopy->getSize()));
    } else {
        Directory *tmpsourceToCopy = new Directory(*dynamic_cast<Directory *>(sourceToCopy));
        tmpsourceToCopy->setParent(destDir);
    }
}

////returns cd toString
string CpCommand::toString() {
    return "cp";
}
