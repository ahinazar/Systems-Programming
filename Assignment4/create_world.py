import os
import sys
import sqlite3

_id = 1


def createDb(conn):
    conn.executescript("""
        CREATE TABLE tasks(
            id              integer             PRIMARY KEY,
            task_name       varchar(255)        NOT NULL,
            worker_id       integer             REFERENCES workers(id),
            time_to_make    integer             NOT NULL,
            resource_name   varchar(255)        NOT NULL,
            resource_amount integer             NOT NULL
        );
 
        CREATE TABLE workers(
            id      integer         PRIMARY KEY,
            name    varchar(255)    NOT NULL,
            status  varchar(255)    NOT NULL
        );
 
        CREATE TABLE resources(
            name    varchar(255)    PRIMARY KEY,
            amount  integer         NOT NULL
        );
    """)


def insertIntoResources(conn, line_parts):
    conn.execute("""INSERT INTO resources(name, amount)
    VALUES (?,?)""", [line_parts[0], line_parts[1]])


def insertIntoWorker(conn, line_parts):
    conn.execute("""INSERT INTO workers(id, name, status)
    VALUES (?,?,?)""", [line_parts[1], line_parts[2], "idle"])


def insertIntoTask(conn, line_parts):
    global _id
    conn.execute("""INSERT INTO tasks(id, task_name, worker_id, time_to_make, resource_name, resource_amount)
    VALUES (?,?,?,?,?,?)""", [_id, line_parts[0], line_parts[1], line_parts[4], line_parts[2], line_parts[3]])
    _id += 1


def insertIntoTables(conn, line_parts):
    if len(line_parts) == 2:
        insertIntoResources(conn, line_parts)
    elif len(line_parts) == 3:
        insertIntoWorker(conn, line_parts)
    elif len(line_parts) == 5:
        insertIntoTask(conn, line_parts)


def splitsLine(line):  # SPLITS EACH LINE BY ','
    line_parts = line.replace("\n", "").split(",")
    return line_parts


def initDb(conn, f):
    for line in f:
        line_parts = splitsLine(line)
        insertIntoTables(conn, line_parts)


def main():
    try:
        with open(sys.argv[1]) as input:
            if not os.path.isfile('world.db'):
                conn = sqlite3.connect('world.db')
                createDb(conn)
                initDb(conn, input)
                conn.commit()
                conn.close()

    except sqlite3.Error as error:
        exit()


if __name__ == '__main__':
    main()
