package bgu.spl.a2;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class PromiseTest {

    @Test
    public void resolve() {
        Promise<Integer> p = new Promise<>();
        try {
            p.resolve(3);
            try {
                p.resolve(4);
                Assert.fail("resolve()- resolve twice");//resolved a resolved promise
            } catch (IllegalStateException ex) { //should not change resolved promise
                try {
                    int pValue = p.get();
                    assertEquals(pValue, 3);
                }catch (Exception ex1){ //something wrong
                    Assert.fail("get() - throws Exception");
                }
            }catch (Exception ex) {//should throw IllegalStateException
                Assert.fail("resolve() - throws another exception from IllegalStateException");
            }
        } catch (Exception ex) { //didnt resolve at all
            Assert.fail("resolve() - throws Exception");
        }
    }

    @Test
    public void get() {
        Promise<Integer> p = new Promise<>();
        try {
            int x = p.get();
        } catch (IllegalStateException ex) { //promise not resolved - nothing to get
            try {
                p.resolve(3);
                try {
                    int pValue = p.get();
                    assertEquals(pValue, 3);
                } catch (IllegalStateException ex1) { // get is resolved - needs to get something back
                    Assert.fail("after resolve() - get() throws IllegalStateException");
                } catch (Exception ex1) {//something wrong with get()
                    Assert.fail("after resolve() - get() throws another exception from IllegalStateException");
                }
            } catch (Exception ex2) { //something wrong while resolve
                Assert.fail("resolve() throws another exception from IllegalStateException");
            }
        } catch (Exception ex) { //something wrong - supposed to throw IllegalStateException
            Assert.fail("before resolve() -get() throws another exception from IllegalStateException");
        }
    }

    @Test
    public void isResolved() {
        Promise<Integer> p = new Promise<>();
        try {
            boolean pResolved = p.isResolved(); //promise not resolve yet
            if (!pResolved) {
                try {
                    p.resolve(5);
                    try {
                        pResolved = p.isResolved();
                        assertTrue(pResolved);
                    } catch (Exception ex1) { //resolved -isResolved() supposed return true
                        Assert.fail("after resolve() - isResolved() throws another exception from IllegalStateException");
                    }
                } catch (Exception ex2) { //something wrong with resolve()
                    Assert.fail("resolve() throws another exception from IllegalStateException");
                }
            }
        } catch (Exception ex) { //something wrong with isResolved()
            Assert.fail("isResolved() throws another exception from IllegalStateException");
        }
    }

    @Test
    public void subscribe() {
        Promise<Integer> p = new Promise<>();
        try {
            int[] countrCallback={0}; //to see when callbacks are called, and how many of them got called
            for (int i = 0; i < 3; i++) { //subscribe supposed to enter the callbacks into some data structure
                p.subscribe(()->{ countrCallback[0]++;});
            }
            try {
                assertEquals(countrCallback[0],0); //promise not resolved - no callbacks called
                p.resolve(5);
                assertEquals(countrCallback[0],3);//promise resolved - 3 callbacks should be called
                try {
                    p.subscribe(()->{
                            throw new IllegalArgumentException(); //to see if called immediately
                    });
                    Assert.fail("subscribe() - call back should be called");
                } catch (IllegalArgumentException ex) { //after resolving the callback called immediately
                    assertTrue(true);
                } catch (Exception ex) { //something wrong with subscribe() - should call the callback immediately
                    Assert.fail("subscribe() throws Exception");
                }
            } catch (Exception ex) { //something wrong with resolve()
                Assert.fail("resolve() throws Exception");
            }
        } catch (Exception ex) { //something wrong with subscribe()
            Assert.fail("subscribe() throws Exception");
        }
    }

}