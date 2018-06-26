#include "Files.h"

using namespace std;

////Constructor
File::File(string name, int size) : BaseFile(name), size(size) {}// Constructor

////Destructor
File::~File() {}

////returns file's size
int File::getSize() {
    return this->size;
}

//// function for use at cp,cd, etc..
string File::typeOf() {
    return "File";
}