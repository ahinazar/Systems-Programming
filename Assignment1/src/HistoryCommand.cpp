#include "Commands.h"
#include <iostream>

using namespace std;

////Constructor
HistoryCommand::HistoryCommand(string args, const vector<BaseCommand *> &history) : BaseCommand(args),
                                                                                    history(history) {}

////executing command - prints history commands
void HistoryCommand::execute(FileSystem &fs) {
    for (int i = 0; i < signed(history.size()); i++) {
        cout << i << "\t";
        if (history[i]->toString() != "error") {
            cout << history[i]->toString() << ' ';
        }
        cout << history[i]->getArgs() << endl;
    }
}

////returns cd toString
string HistoryCommand::toString() {
    return "history";
}
