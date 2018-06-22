package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;

public class ParticipatingInCourse extends Action {

    private String studentId;
    private String course;
    private LinkedList<String> grade;

    /**
     *
     * @param studentId student's ID
     * @param course course's name
     * @param grade list of grades
     */
    public ParticipatingInCourse(String studentId, String course, LinkedList<String> grade) {
        setActionName("Participate In Course");
        this.course = course;
        this.studentId = studentId;
        this.grade = grade;

    }

    /**
     * executing operation
     */
    public void start() {
        CoursePrivateState thisCoursePrivateState = ((CoursePrivateState) this.getActorState());
        if (thisCoursePrivateState.getAvailableSpots() > 0) {

            LinkedList<Action> actionsBefore = new LinkedList<>();
            Action checkPrequisitesForStudentAction = new CheckPrequisitesForStudentAction((LinkedList<String>) thisCoursePrivateState.getPrequisites());
            sendMessage(checkPrequisitesForStudentAction, studentId, ((CheckPrequisitesForStudentAction) checkPrequisitesForStudentAction).getActorState());
            actionsBefore.add(checkPrequisitesForStudentAction);
            then(actionsBefore, () -> {

                if (!thisCoursePrivateState.getRegStudents().contains(studentId) && checkPrequisitesForStudentAction.getResult().get().equals(true) && thisCoursePrivateState.getAvailableSpots() > 0) {
                    thisCoursePrivateState.setAvailableSpots(thisCoursePrivateState.getAvailableSpots() - 1);
                    thisCoursePrivateState.getRegStudents().add(studentId);
                    thisCoursePrivateState.setRegistered(thisCoursePrivateState.getRegistered() + 1);

                    LinkedList<Action> actionsBefore2 = new LinkedList<>();
                    Action insertGradeToStudentCourseAction = new InsertGradeToStudentCourseAction(course, grade.getFirst());
                    sendMessage(insertGradeToStudentCourseAction, studentId, ((InsertGradeToStudentCourseAction) insertGradeToStudentCourseAction).getActorState());
                    actionsBefore2.add(insertGradeToStudentCourseAction);
                    then(actionsBefore2, () -> {
                        complete(true);
                    });
                } else {

                    complete(false);
                }
            });
        } else {
            complete(false);
        }
    }

    /**
     *
     * @return actor's private state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }

}
