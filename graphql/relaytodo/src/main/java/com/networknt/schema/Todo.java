package com.networknt.subscription.schema;

/**
 * Created by steve on 26/03/17.
 */
public class Todo {

    private String id;

    private String text;

    private boolean complete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
