package com.thomasporro.avoid;


/**
 * Enum class used to pass the actions to do to the main activity
 */
public enum Actions {
    //TODO retrieve this strings from the resources
    CHANGE_COLOR("-89833995"),
    SCALE_UP("1931229839"),
    SCALE_DOWN("1931229840"),
    RETURN_NORMAL("-1845955904"),
    NOT_VALID("");


    private String value;

    /**
     * Constructor
     *
     * @param value The number to assign to the action
     */
    Actions(String value){
        this.value = value;
    }

    /**
     * Return the String
     *
     * @return the value of the constant
     */
    String getString(){
        return this.value;
    }

    /**
     * Check if two actions are equals
     *
     * @param action The  action to compare
     * @return True if the Actions are euqual
     */
    boolean isEqual(Actions action){
        String actionString = action.getString();
        return actionString.equalsIgnoreCase(this.value);
    }

    /**
     * Check if a String is equal to the current Actions
     *
     * @param string The string to compare
     * @return True if the string is equal to this.value
     */
    boolean isEqualToString(String string){
        return string.equalsIgnoreCase(this.value);
    }
}