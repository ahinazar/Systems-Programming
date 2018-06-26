#include "Files.h"

using namespace std;

////Destructor
BaseFile::~BaseFile() {}

////Constructor
BaseFile::BaseFile(string name) : name(name) {}

////returns file's name
string BaseFile::getName() const {
    return this->name;
}

////setting file's name
void BaseFile::setName(string newName) {
    this->name = newName;
}



