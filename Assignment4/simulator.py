import os
import sqlite3


def tasks_and_workers(conn):
    return conn.execute("""SELECT tasks.id, tasks.task_name, tasks.time_to_make, tasks.resource_name, tasks.resource_amount, workers.id, workers.name, workers.status
                        FROM tasks INNER JOIN workers ON tasks.worker_id = workers.id""").fetchall()


def deleteTask(conn, task_id):
    conn.execute("""DELETE FROM tasks
                WHERE id=? """, [task_id])


def clean_tasks(conn):
    workers = conn.execute("""SELECT w.id, w.name 
                            FROM tasks as t JOIN workers as w ON w.id=t.worker_id 
                            WHERE t.time_to_make=?""", [0]).fetchall()
    for worker in workers:
        conn.execute("UPDATE workers SET status=? WHERE id=?", ["idle", worker[0]])
        print(worker[1], "says: All Done!")

    conn.execute("DELETE FROM tasks WHERE time_to_make=0 ")


def countDownTimeToMake(conn, task_id, time_to_make):
    conn.execute("""UPDATE tasks
                SET time_to_make = ?
                WHERE id =? """, [time_to_make - 1, task_id])


def selectAmount(conn, resource_name):
    return conn.execute("""SELECT amount FROM resources
                        WHERE name =? """, [resource_name]).fetchone()[0]


def reduceByOneResource(conn, resource_name):
    resource_amount = selectAmount(conn, resource_name)
    resource_amount = resource_amount - 1
    conn.execute("""UPDATE resources
                SET amount = ?
                WHERE name =? """, [resource_amount, resource_name])


def selectStatus(conn, worker_id):
    return conn.execute("""SELECT status FROM workers
                        WHERE id =? """, [worker_id]).fetchone()[0] == "idle"


def makeWorkerToBusy(conn, worker_id, worker_name):
    if selectStatus(conn, worker_id):
        conn.execute("""UPDATE workers
                    SET status = ?
                    WHERE id =? """, ["busy", worker_id])
        print(worker_name, "says: work work")


def countTasks(conn):
    return len(conn.execute("""SELECT 1 FROM tasks""").fetchall())


def main():
    if not os.path.isfile('world.db'):
        exit()

    conn = sqlite3.connect('world.db')
    while os.path.isfile('world.db') and countTasks(conn) > 0:
        ids = list()
        for task in tasks_and_workers(conn):
            if task[5] in ids:
                continue
            ids.append(task[5])

            if task[7] == "idle":  # worker status is idle
                makeWorkerToBusy(conn, task[5], task[6])
                reduceByOneResource(conn, task[3])
            else:
                countDownTimeToMake(conn, task[0], task[2])
                print(task[6], "is busy", task[1] + "...")

        clean_tasks(conn)

    conn.commit()
    conn.close()


if __name__ == '__main__':
    main()
