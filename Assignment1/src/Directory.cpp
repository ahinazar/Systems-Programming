#include <iostream>
#include "Files.h"
#include <algorithm>
#include "GlobalVariables.h"

using namespace std;

//#################comprators for sorting functions#######################

bool compByName(BaseFile *file1, BaseFile *file2) {
    return file1->getName() < file2->getName();
}

bool compBySize(BaseFile *file1, BaseFile *file2) {
    return file1->getSize() < file2->getSize();
}
//########################################################################

//########################################################################

//// function for use at cp,cd, etc..
string Directory::typeOf() {
    return "Directory";
}

//// for use at destructor and move/copy assignment operator
void Directory::clean() {
    for (BaseFile *toDelete:children) {
        delete toDelete;
        toDelete = nullptr;
    }
    this->parent = nullptr;
    this->children.clear();
}

//// for use at copy constructor and copy assignment operator
void Directory::copy(const Directory &dir) {
    this->children.clear();

    vector<BaseFile *>::const_iterator curr = dir.children.begin();
    vector<BaseFile *>::const_iterator end = dir.children.end();

    BaseFile *newFile;
    while (curr != end) {
        if ((*curr)->typeOf() == "File") {
            newFile = new File((*curr)->getName(), (*curr)->getSize());
        } else {
            Directory *dir = (Directory *) *curr;
            newFile = new Directory(*dir);
            ((Directory*)newFile)->parent = this;
        }
        this->children.push_back(newFile);
        ++curr;
    }
}

//// for use at move constructor operator
void Directory::steal(Directory &dir) {
    this->children = dir.children;
    dir.children.clear();
    this->parent = dir.parent;
    this->parent->addFile(this);

    bool deleted = false;

    for(int i=0; i<signed(dir.parent->children.size()) && (!deleted) ; i++){
        if(dir.parent->children[i] == &dir){
            dir.parent->children.erase(dir.parent->children.begin() + i);
            deleted = true;
        }
    }

    dir.parent = nullptr;
}

//########################################################################

//########################RULE OF 5#####################################

////Move constructor
Directory::Directory(Directory &&copy) : BaseFile(copy.getName()), children(), parent() {
    if (verbose == 1 || verbose == 3) {
        cout << "Directory::Directory(Directory &&copy):BaseFile(copy.getName())" << endl;
    }
    steal(copy);
}

////Copy constructor
Directory::Directory(const Directory &toCopyFrom) : BaseFile(toCopyFrom.getName()), children(),
                                                    parent(toCopyFrom.getParent()) {
    if (verbose == 1 || verbose == 3) {
        cout
                << "Directory::Directory(const Directory &toCopyFrom):BaseFile(toCopyFrom.getName()),children(),parent(toCopyFrom.getParent())"
                << endl;
    }
    copy(toCopyFrom);
}

////Destructor
Directory::~Directory() {
    if (verbose == 1 || verbose == 3) {
        cout << "Directory::~Directory()" << endl;
    }
    clean();
}

////copy assignment operator
Directory &Directory::operator=(const Directory &toAssign) {
    if (verbose == 1 || verbose == 3) {
        cout << "Directory& Directory::operator =(const Directory &toAssign)" << endl;
    }
    if (this != &toAssign) {
        clean();
        this->setName(toAssign.getName());
        this->setParent(toAssign.getParent());
        copy(toAssign);
    }
    return *this;
}

////move assignment operator
Directory &Directory::operator=(Directory &&toAssign) {
    if (verbose == 1 || verbose == 3) {
        cout << "Directory& Directory::operator =(Directory &&toAssign)" << endl;
    }
    if (this != &toAssign) {
        clean();
        steal(toAssign);
    }
    return *this;
}


//########################################################################

//// Constructor
Directory::Directory(string name, Directory *parent) : BaseFile(name), children(), parent(parent) {
    if (parent != nullptr) {
        parent->addFile(this);
    }
}

//// Return a pointer to the parent of this directory
Directory *Directory::getParent() const {
    return this->parent;
}

//// Change the parent of this directory
void Directory::setParent(Directory *newParent) {
    if (newParent != nullptr) {
        if (this->parent != nullptr) {
            parent->removeFile(this);
        }
        newParent->addFile(this);
        parent = newParent;
    }
}

//// Add the file to children
void Directory::addFile(BaseFile *file) {
    if (file != nullptr) {
        children.push_back(file);
    }
    if(file->typeOf() == "Directory"){
        ((Directory*)file)->parent = this;
    }
}

//// Remove the file with the specified name from children
void Directory::removeFile(string name) {
    for (int i = 0; i < signed(children.size()); i++) {
        if (children[i]->getName() == name) {
            delete children[i]; //if we are on the heap deletion should be recursive on all children - destructor
            children.erase(children.begin() + i);
        }
    }
}

//// Remove the file from children
void Directory::removeFile(BaseFile *file) {
    if (file != nullptr) {
        for (int i = 0; i < signed(children.size()); i++) {
            if (children[i] == file) {
                delete file; //if we are on the heap deletion should be recursive on all children - destructor
                children.erase(children.begin() + i);
            }
        }
    }
}

//// Sort children by name alphabetically (not recursively)
void Directory::sortByName() {
    sort(children.begin(), children.end(), compByName);
}

//// Sort children by size (not recursively)
void Directory::sortBySize() {
    sort(children.begin(), children.end(), compBySize);
}

//// Return children
vector<BaseFile *> Directory::getChildren() {
    return this->children;
}

//// Return the size of the directory (recursively)
int Directory::getSize() {
    int totalSize = 0;
    for (int i = 0; i < signed(this->children.size()); i++) { //file has a getSize of his own. cases no needed.
        totalSize += children[i]->getSize();
    }
    return totalSize;
}

//// Return the path from the root to this
string Directory::getAbsolutePath() {
    string path = "";
    if (parent == nullptr) {
        return this->getName();
    } else { //check my father recursively
        path = parent->getAbsolutePath() + "/" + this->getName();//taking the value of parent (Directory)
    }
    return path;
}
