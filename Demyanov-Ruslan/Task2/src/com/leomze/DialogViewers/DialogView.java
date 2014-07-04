package com.leomze.DialogViewers;




public class DialogView {
    private static final int CREATE_USER = 1;
    private static final int MODIFY_USER = 2;
    private static final int ADD_EVENT = 3;
    private static final int ADD_RANDOM_EVENT = 4;
    private static final int REMOVE_EVENT = 5;
    private static final int CLONE_EVENT = 6;
    private static final int SYNC = 7;
    private static final int SAVE = 8;

    public void dialogChooser(int i){

        switch (i){
            case CREATE_USER:
                new CreateUser().start();
                break;
            case MODIFY_USER:
                new ModifyUser().start();
                break;
            case ADD_EVENT:
                new AddEvent().start();
                break;
            case ADD_RANDOM_EVENT:
                new AddRandomEvent().start();
                break;
            case REMOVE_EVENT:
                new RemoveEvent().start();
                break;
            case CLONE_EVENT:
                new CloneEvent().start();
                break;
            case SYNC:
                break;
            case SAVE:
                break;



        }

    }


}
