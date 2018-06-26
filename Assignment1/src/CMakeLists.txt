cmake_minimum_required(VERSION 3.8)
project(untitled1)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp BaseFile.cpp Files.h Directory.cpp File.cpp FileSystem.cpp FileSystem.h Commands.h BaseCommand.cpp PwdCommand.cpp CdCommand.cpp LsCommand.cpp MkdirCommand.cpp MkfileCommand.cpp CpCommand.cpp MvCommand.cpp RenameCommand.cpp RmCommand.cpp HistoryCommand.cpp VerboseCommand.cpp ErrorCommand.cpp ExecCommand.cpp Environment.cpp Environment.h GlobalVariables.h)
add_executable(untitled1 ${SOURCE_FILES})