package net.centralfloridaattorney.python;

//import net.centralfloridaattorney.toolbag.context.ContextManager;

import net.centralfloridaattorney.GLOBAL_VARIABLES;

import java.util.LinkedList;
import java.util.List;

public class Stack {
    private List<String> internalList = new LinkedList<String>();


    //value is only added when the stack isEmpty
    //Returns true when an value is added to the stack
    //Returns false when an value can not be added to the stack
    //Elements can not be added then there is already an value in the stack
    public boolean push(String value) {
        if (isEmpty()) {
            internalList.add(0, value);
            return true;
        } else {
            return false;
        }
    }

    //This destructive operation is called by py4j in IPC.py
    public String pop() {
        if (!isEmpty()) {
            return internalList.remove(0);
        } else {
            return GLOBAL_VARIABLES.DEFAULT_VALUE;
        }
    }

    public List<String> getInternalList() {
        return internalList;
    }

    public boolean isEmpty() {
        return internalList.isEmpty();
    }
}