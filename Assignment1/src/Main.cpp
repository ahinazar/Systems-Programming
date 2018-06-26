#include <iostream>
#include "Environment.h"

using namespace std;

unsigned int verbose = 0;

int main(int, char **) {
    Environment env;
    env.start();
    return 0;
}

