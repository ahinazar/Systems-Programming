#include "Environment.h"
#include <iostream>
#include "GlobalVariables.h"

////returns vector of strings splitted by char ( some cases it's '/' and some cases it's ' ')
vector<string> Environment::splitString(string args, string splitBy) {
    vector<string> splitted;
    int start = 0;
    int end = args.size() - 1;
    for (int i = 0; i < signed(args.size()); i++) {
        if (args.substr(i, 1) == " ") {
            splitted.push_back(args.substr(start, i - start));
            start = i + 1;
        } else if (i == end) {
            splitted.push_back(args.substr(start, i - start + 1));
        }
    }
    return splitted;
}

//// for use at destructor and move/copy assignment operator
void Environment::clean() {
    for (BaseCommand *cmd: commandsHistory) {
        delete cmd;
        cmd = nullptr;
    }
    this->commandsHistory.clear();
}

//// for use at copy constructor and copy assignment operator
void Environment::copy(const Environment &env) {

    this->commandsHistory.clear();

    vector<BaseCommand *>::const_iterator curr = env.commandsHistory.begin();
    vector<BaseCommand *>::const_iterator end = env.commandsHistory.end();

    while (curr != end) {
        if ((*curr)->toString() == "pwd") {
            PwdCommand *newCommand = new PwdCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "cd") {
            CdCommand *newCommand = new CdCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "ls") {
            LsCommand *newCommand = new LsCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "mkdir") {
            MkdirCommand *newCommand = new MkdirCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "mkfile") {
            MkfileCommand *newCommand = new MkfileCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "cp") {
            CpCommand *newCommand = new CpCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "mv") {
            MvCommand *newCommand = new MvCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "rename") {
            RenameCommand *newCommand = new RenameCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "rm") {
            RmCommand *newCommand = new RmCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "history") {
            HistoryCommand *newCommand = new HistoryCommand((*curr)->getArgs(), env.getHistory());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "verbose") {
            VerboseCommand *newCommand = new VerboseCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "error") {
            ErrorCommand *newCommand = new ErrorCommand((*curr)->getArgs());
            this->addToHistory(newCommand);
        } else if ((*curr)->toString() == "exec") {
            ExecCommand *newCommand = new ExecCommand((*curr)->getArgs(), env.getHistory());
            this->addToHistory(newCommand);
        }
        ++curr;
    }
}

//// for use at move constructor operator
void Environment::steal(Environment &env) {
    this->commandsHistory = env.commandsHistory;
    env.commandsHistory.clear();
    this->fs = move(env.fs);
}

//########################RULE OF 5#####################################

////Move constructor
Environment::Environment(Environment &&copy) : commandsHistory(), fs() {
    if (verbose == 1 || verbose == 3) {
        cout << "Environment::Environment(Environment &&copy)" << endl;
    }
    steal(copy);
}

////Copy constructor
Environment::Environment(const Environment &toCopyFrom) : commandsHistory(), fs(toCopyFrom.fs) {
    if (verbose == 1 || verbose == 3) {
        cout << "Environment::Environment(const Environment &toCopyFrom):fs(toCopyFrom.fs),commandsHistory()" << endl;
    }
    copy(toCopyFrom);
}

////Destructor
Environment::~Environment() {
    if (verbose == 1 || verbose == 3) {
        cout << "Environment::~Environment()" << endl;
    }
    clean();
}

////copy assignment operator
Environment &Environment::operator=(const Environment &toAssign) {
    if (verbose == 1 || verbose == 3) {
        cout << "Environment& Environment::operator =(const Environment &toAssign)" << endl;
    }
    if (this != &toAssign) {
        clean();
        fs = toAssign.fs;
        copy(toAssign);
    }
    return *this;
}

////move assignment operator
Environment &Environment::operator=(Environment &&toAssign) {
    if (verbose == 1 || verbose == 3) {
        cout << "Environment& Environment::operator =(Environment &&toAssign)" << endl;
    }
    if (this != &toAssign) {
        clean();
        steal(toAssign);
    }
    return *this;
}

//########################################################################

////Constructor
Environment::Environment() : commandsHistory(), fs() {}

////returns nothing and operates system
void Environment::start() {

    string userInput;

    while (userInput != "exit") {

        if (fs.getWorkingDirectory().getAbsolutePath() == fs.getRootDirectory().getAbsolutePath()) {
            cout << "/" << ">";
        } else {
            cout << fs.getWorkingDirectory().getAbsolutePath() << ">";
        }

        getline(std::cin, userInput);

        if (userInput == "exit") { //stop this process
            break;
        }

        if (verbose == 2 || verbose == 3) { //print user commend
            cout << userInput << endl;
        }

        vector<string> splitBySpace = splitString(userInput,
                                                  " "); //splitting user input to recognize what is the commend and what is argument

        if (splitBySpace.empty()) {}
        else if (splitBySpace.size() == 1) {
            if (splitBySpace[0] == "pwd") {
                PwdCommand *cmd = new PwdCommand("");
                cmd->execute(fs);
                addToHistory(new PwdCommand(""));
                delete cmd;
            } else if (splitBySpace[0] == "history") {
                HistoryCommand *cmd = new HistoryCommand("", commandsHistory);
                cmd->execute(fs);
                addToHistory(new HistoryCommand("", commandsHistory));
                delete cmd;
            } else if (splitBySpace[0] == "ls") {
                LsCommand *cmd = new LsCommand("");
                cmd->execute(fs);
                addToHistory(new LsCommand(""));
                delete cmd;
            } else if (userInput == "exit") {
                return;
            } else {
                ErrorCommand *cmd = new ErrorCommand(userInput);
                cmd->execute(fs);
                addToHistory(new ErrorCommand(userInput));
                delete cmd;
            }

        } else if (splitBySpace.size() == 2) {
            if (splitBySpace[0] == "cd") {
                CdCommand *cmd = new CdCommand(splitBySpace[1]);
                cmd->execute(fs);
                addToHistory(new CdCommand(splitBySpace[1]));
                delete cmd;
            } else if (splitBySpace[0] == "ls") {
                LsCommand *cmd = new LsCommand(splitBySpace[1]);
                cmd->execute(fs);
                addToHistory(new LsCommand(splitBySpace[1]));
                delete cmd;
            } else if (splitBySpace[0] == "mkdir") {
                MkdirCommand *cmd = new MkdirCommand(splitBySpace[1]);
                cmd->execute(fs);
                addToHistory(new MkdirCommand(splitBySpace[1]));
                delete cmd;
            } else if (splitBySpace[0] == "rm") {
                RmCommand *cmd = new RmCommand(splitBySpace[1]);
                cmd->execute(fs);
                addToHistory(new RmCommand(splitBySpace[1]));
                delete cmd;
            } else if (splitBySpace[0] == "verbose") {
                VerboseCommand *cmd = new VerboseCommand(splitBySpace[1]);
                cmd->execute(fs);
                addToHistory(new VerboseCommand(splitBySpace[1]));
                delete cmd;
            } else if (splitBySpace[0] == "exec") {
                ExecCommand *cmd = new ExecCommand((splitBySpace[1]), commandsHistory);
                cmd->execute(fs);
                addToHistory(new ExecCommand((splitBySpace[1]), commandsHistory));
                delete cmd;
            } else {
                ErrorCommand *cmd = new ErrorCommand(userInput);
                cmd->execute(fs);
                addToHistory(new ErrorCommand(userInput));
                delete cmd;
            }
        } else if (splitBySpace.size() == 3) {
            if (splitBySpace[0] == "ls") {
                LsCommand *cmd = new LsCommand((splitBySpace[1] + " " + splitBySpace[2]));
                cmd->execute(fs);
                addToHistory(new LsCommand((splitBySpace[1] + " " + splitBySpace[2])));
                delete cmd;
            } else if (splitBySpace[0] == "mkfile") {
                MkfileCommand *cmd = new MkfileCommand((splitBySpace[1] + " " + splitBySpace[2]));
                cmd->execute(fs);
                addToHistory(new MkfileCommand((splitBySpace[1] + " " + splitBySpace[2])));
                delete cmd;
            } else if (splitBySpace[0] == "cp") {
                CpCommand *cmd = new CpCommand((splitBySpace[1] + " " + splitBySpace[2]));
                cmd->execute(fs);
                addToHistory(new CpCommand((splitBySpace[1] + " " + splitBySpace[2])));
                delete cmd;
            } else if (splitBySpace[0] == "mv") {
                MvCommand *cmd = new MvCommand((splitBySpace[1] + " " + splitBySpace[2]));
                cmd->execute(fs);
                addToHistory(new MvCommand((splitBySpace[1] + " " + splitBySpace[2])));
                delete cmd;
            } else if (splitBySpace[0] == "rename") {
                RenameCommand *cmd = new RenameCommand((splitBySpace[1] + " " + splitBySpace[2]));
                cmd->execute(fs);
                addToHistory(new RenameCommand((splitBySpace[1] + " " + splitBySpace[2])));
                delete cmd;
            } else {
                ErrorCommand *cmd = new ErrorCommand(userInput);
                cmd->execute(fs);
                addToHistory(new ErrorCommand(userInput));
                delete cmd;
            }
        } else {
            ErrorCommand *cmd = new ErrorCommand((userInput));
            cmd->execute(fs);
            addToHistory(new ErrorCommand((userInput)));
            delete cmd;
        }
    }
}

//// Get a reference to the file system
FileSystem &Environment::getFileSystem() {
    return fs;
}

////// Add a new command to the history
void Environment::addToHistory(BaseCommand *command) {
    commandsHistory.push_back(command);
}

//// Return a reference to the history of commands
const vector<BaseCommand *> &Environment::getHistory() const {
    return commandsHistory;
}
