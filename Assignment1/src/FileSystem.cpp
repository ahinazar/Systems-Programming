#include "FileSystem.h"
#include "Commands.h"

using namespace std;

//####################################################################################################

////function that helps copy assignment operator
void FileSystem::clean() {
    delete rootDirectory;
    workingDirectory = nullptr;
    rootDirectory = nullptr;
}

//####################################################################################################

//########################################## RULE OF 5 ###############################################
//// Destructor
FileSystem::~FileSystem() {
    clean();
}

//// Copy constructor
FileSystem::FileSystem(const FileSystem &copy) : rootDirectory(new Directory(*(copy.rootDirectory))),
                                                 workingDirectory(copy.rootDirectory) {
    CdCommand cd(copy.getWorkingDirectory().getAbsolutePath());
    cd.execute(*this);
}

//// Move constractor
FileSystem::FileSystem(FileSystem &&copy) : rootDirectory(copy.rootDirectory), workingDirectory(copy.workingDirectory) {
    copy.rootDirectory = nullptr;
    copy.workingDirectory = nullptr;
}

////copy assignment operator
FileSystem &FileSystem::operator=(const FileSystem &assign) {
    if (&assign != this) {
        clean();
        this->rootDirectory = new Directory(*(assign.rootDirectory));
        CdCommand cd(assign.getWorkingDirectory().getAbsolutePath());
        cd.execute(*this);
    }
    return *this;
}

////move assignment operator
FileSystem &FileSystem::operator=(FileSystem &&toAssign) {
    if (&toAssign != this) {
        clean();
        this->rootDirectory = new Directory(*(toAssign.rootDirectory));
        CdCommand cd(toAssign.getWorkingDirectory().getAbsolutePath());
        cd.execute(*this);
        toAssign.rootDirectory = nullptr;
        toAssign.workingDirectory = nullptr;
    }
    return *this;
}
//####################################################################################################

////Constructor
FileSystem::FileSystem() : rootDirectory(new Directory("", nullptr)), workingDirectory(rootDirectory) {}

//// Return reference to the root directory
Directory &FileSystem::getRootDirectory() const {
    return *rootDirectory;
}

//// Return reference to the working directory
Directory &FileSystem::getWorkingDirectory() const {
    return *workingDirectory;
}

//// Change the working directory of the file system
void FileSystem::setWorkingDirectory(Directory *newWorkingDirectory) {
    this->workingDirectory = newWorkingDirectory;
}