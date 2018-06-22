
#ifndef FILESYSTEM_H_
#define FILESYSTEM_H_

#include "Files.h"

class FileSystem {
private:
    Directory *rootDirectory;
    Directory *workingDirectory;
public:
//####################################################################################
    void clean();

//####################################################################################
//############################## RULE OF 5 ##########################################
    FileSystem(const FileSystem &copy); // Copy constractor
    virtual ~FileSystem();// Destructor
    virtual FileSystem &operator=(const FileSystem &assign); // copy assignment operator
    FileSystem(FileSystem &&copy); // Move constractor
    FileSystem &operator=(FileSystem &&toAssign);//move assignment operator
//####################################################################################
    FileSystem();

    Directory &getRootDirectory() const; // Return reference to the root directory
    Directory &getWorkingDirectory() const; // Return reference to the working directory
    void setWorkingDirectory(Directory *newWorkingDirectory); // Change the working directory of the file system
};


#endif
