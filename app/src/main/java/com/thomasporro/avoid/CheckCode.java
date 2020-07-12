package com.thomasporro.avoid;


class CheckCode {
    //Class variables
    private String current_guess;

    CheckCode(){
        current_guess = "";
    }

    /**
     * Add part of the solution code to the current guess. To do so it's used the hash function
     * provided from java beacuse in the app are not saved important data
     *
     * @param digit the part of the code to add to the guess
     */
    void addDigit(String digit){
        current_guess += Integer.toString(digit.hashCode());
    }

    /**
     * Remove all the digit added
     */
    void clearHistory(){
        current_guess = "";
    }

    /**
     * Try to guess the code
     * @return The Actions corresponding, Null if there is no action that is equal to the string
     */
    Actions tryGuess(){
        //System.out.println(Actions.CHANGE_COLOR.getString());
        current_guess = Integer.toString(current_guess.hashCode());
        if(this.current_guess.equalsIgnoreCase(Actions.CHANGE_COLOR.getString())){
            return Actions.CHANGE_COLOR;
        } else if(this.current_guess.equalsIgnoreCase(Actions.SCALE_DOWN.getString())){
            return Actions.SCALE_DOWN;
        }else if(this.current_guess.equalsIgnoreCase(Actions.SCALE_UP.getString())){
            return Actions.SCALE_UP;
        }else if(this.current_guess.equalsIgnoreCase((Actions.RETURN_NORMAL.getString()))){
            return Actions.RETURN_NORMAL;
        } else{
            return Actions.NOT_VALID;
        }
    }
}
