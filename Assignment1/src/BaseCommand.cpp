#include "Commands.h"
#include <iostream>

using namespace std;

////Constractor
BaseCommand::BaseCommand(string args) : args(args) {}

////Destructor
BaseCommand::~BaseCommand() {}

//// returns commend's args
string BaseCommand::getArgs() {
    return args;
}

////executing commend
void BaseCommand::execute(FileSystem &fs) {}

////returns vector of strings splitted by char ( some cases it's '/' and some cases it's ' ')
vector<string> BaseCommand::splitString(string args, char splitBy) {
    vector<string> splitted;
    int start = 0;
    int end = args.size() - 1;
    for (int i = 0; i < signed(args.size()); i++) {
        if (args[i] == splitBy) {
            splitted.push_back(args.substr(start, i - start));
            start = i + 1;
        } else if (i == end) {
            splitted.push_back(args.substr(start, i - start + 1));
        }
    }
    return splitted;
}
//######################################################################################################################

////returns last father of path's file or directory, or null if path is not valid
Directory *BaseCommand::isValidPath(string path, FileSystem &fs, Directory *workingDirectory) {
    workingDirectory = &fs.getWorkingDirectory();
    if (path == workingDirectory->getName()) {  //it's working directory
        return workingDirectory;
    } else if (path[0] == '/') { //start from root case
        workingDirectory = &fs.getRootDirectory();
        path = path.substr(1, path.length());
    }
    int i = 0;
    vector<string> splittedPath = splitString(path, '/'); //split path by slash ('/')
    while (i < signed(splittedPath.size())) { // running on path's directory names
        if (splittedPath[i] == ".." && workingDirectory->getParent() != nullptr) { //it's '..' so go to parent
            workingDirectory = workingDirectory->getParent();
        } else if (workingDirectory->getChildren().size() == 0) { //not exist
            return nullptr;
        } else {
            int j;
            for (j = 0; j < signed(workingDirectory->getChildren().size()); j++) { //check at children'n names. if it fits, go and check their childs...
                if (splittedPath[i] == workingDirectory->getChildren()[j]->getName() &&
                    workingDirectory->getChildren()[j]->typeOf() == "Directory") {
                    workingDirectory = (Directory *) workingDirectory->getChildren()[j];
                    break;
                } else if (j == signed(workingDirectory->getChildren().size() - 1)) { //not exist
                    return nullptr;
                }
            }
        }
        i++;
    }
    return workingDirectory;
}

