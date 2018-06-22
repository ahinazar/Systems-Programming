package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;

public class CheckPrequisitesForStudentAction extends Action {

    private LinkedList<String> prequisites;

    /**
     * Constructor
     *
     * @param prequisites for linked list prequisites field
     */
    public CheckPrequisitesForStudentAction(LinkedList<String> prequisites) {
        setActionName("Check Prequisites For Student Action");
        this.prequisites = prequisites;
    }

    /**
     * executing operation
     */
    public void start() {
        for (String prequisiteCourse : prequisites) {
            if (((StudentPrivateState) this.getActorState()).getGrades().get(prequisiteCourse) == null) {
                complete(false);
                return;
            }
        }
        complete(true);
    }

    /**
     * @return actor state
     */
    public PrivateState getActorState() {
        return this.actorState;
    }
}
