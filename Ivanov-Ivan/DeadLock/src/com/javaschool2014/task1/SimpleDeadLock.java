package com.javaschool2014.task1;

public class SimpleDeadLock {

    static class LockedObject {

        private final String name;

        public LockedObject(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public synchronized void ping(LockedObject bower) {
            System.out.println(this.name + bower.getName());
            bower.pingBack(this);
        }

        public synchronized void pingBack(LockedObject bower) {
            System.out.println(this.name + bower.getName());
        }

    }

    public static void main(String[] args) {

        final LockedObject one = new LockedObject("One");
        final LockedObject two = new LockedObject("Two");

            new Thread(new Runnable() {
                public void run() { one.ping(two); }
            }).start();

            new Thread(new Runnable() {
                public void run() { two.ping(one); }
            }).start();

    }

}
