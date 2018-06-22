package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.Arrays;
import java.util.LinkedList;

public class RegisterWithPreferences extends Action {

    private String studentId;
    private LinkedList<String> preferences;
    private LinkedList<String> grade;
    private Promise lastActionSubmittedPromise = null;
    public String theCourse;
    /**
     *
     * @param studentId student's ID
     * @param preferences list of preferences courses
     * @param grade list of grades according to preferences
     */
    public RegisterWithPreferences(String studentId, LinkedList<String> preferences, LinkedList<String> grade) {
        setActionName("Register With Preferences");
        this.preferences = preferences;
        this.studentId = studentId;
        this.grade = grade;
    }

    /**
     * executing operation
     */
    public void start() {
        if (lastActionSubmittedPromise != null && lastActionSubmittedPromise.get().equals(true)) {
            complete(true);
        } else if (preferences.isEmpty()) {
            complete(false);
        } else {
            LinkedList<Action> actionsBefore = new LinkedList<>();
            LinkedList<String> gradeOfOneCourse = new LinkedList<>();
            gradeOfOneCourse.add(grade.remove());

            String courseId = preferences.remove();
            theCourse = courseId;

            Action participatingInCourseNewAction = new ParticipatingInCourse(studentId, courseId, gradeOfOneCourse);
            lastActionSubmittedPromise = sendMessage(participatingInCourseNewAction, courseId, new CoursePrivateState());
            actionsBefore.add(participatingInCourseNewAction);
            then(actionsBefore, this::start);
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
