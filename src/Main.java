class animalThread extends Thread {
    private String animalName;
    private int meters;
    private boolean isFinished;

    public animalThread(String name, int priority) {
        this.animalName = name;
        this.meters = 0;
        this.isFinished = false;
        this.setPriority(priority);
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println(animalName + " стартовал! Приоритет: " + this.getPriority());

        while (meters < 100 && !isInterrupted()) {
            meters++;

            if (meters % 10 == 0) {
                System.out.println(animalName + " пробежал " + meters + " метров");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(animalName + " был прерван!");
                break;
            }
        }

        isFinished = true;
        if (meters >= 100) {
            System.out.println("=== " + animalName + " ФИНИШИРОВАЛ! ===");
        }
    }

    public int getMeters() {
        return meters;
    }

    public String getAnimalName() {
        return animalName;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
public class Main {
    public static void main(String[] args) {
        System.out.println("СТАРТ ГОНКИ КРОЛИКА И ЧЕРЕПАХИ");

        animalThread rabbit = new animalThread("Кролик", Thread.MAX_PRIORITY);
        animalThread turtle = new animalThread("Черепаха", Thread.MIN_PRIORITY);

        rabbit.start();
        turtle.start();

        monitorAndAdjustRace(rabbit, turtle);

        try {
            rabbit.join();
            turtle.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ГОНКА ЗАВЕРШЕНА");
        printResults(rabbit, turtle);
    }

    private static void monitorAndAdjustRace(animalThread rabbit, animalThread turtle) {
        while (!rabbit.isFinished() && !turtle.isFinished()) {
            try {
                Thread.sleep(500); // Проверяем каждые 0.5 секунды

                int rabbitMeters = rabbit.getMeters();
                int turtleMeters = turtle.getMeters();

                System.out.println("\nТекущий статус Кролик: " + rabbitMeters +
                        "м, Черепаха: " + turtleMeters + "м");

                if (rabbitMeters > turtleMeters + 20) {
                    rabbit.setPriority(Thread.MIN_PRIORITY);
                    turtle.setPriority(Thread.MAX_PRIORITY);
                    System.out.println("Черепаха отстает! Повышаем приоритет Черепахи: " +
                            turtle.getPriority() + ", понижаем Кролика: " + rabbit.getPriority());

                } else if (turtleMeters > rabbitMeters + 20) {
                    rabbit.setPriority(Thread.MAX_PRIORITY);
                    turtle.setPriority(Thread.MIN_PRIORITY);
                    System.out.println("Кролик отстает! Повышаем приоритет Кролика: " +
                            rabbit.getPriority() + ", понижаем Черепахи: " + turtle.getPriority());

                } else if (Math.abs(rabbitMeters - turtleMeters) <= 5) {
                    rabbit.setPriority(Thread.NORM_PRIORITY);
                    turtle.setPriority(Thread.NORM_PRIORITY);
                    System.out.println("Близкая гонка! Устанавливаем равные приоритеты: " +
                            Thread.NORM_PRIORITY);
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private static void printResults(animalThread rabbit, animalThread turtle) {
        System.out.println("\nРЕЗУЛЬТАТЫ:");
        System.out.println("Кролик: " + rabbit.getMeters() + " метров");
        System.out.println("Черепаха: " + turtle.getMeters() + " метров");

        if (rabbit.getMeters() > turtle.getMeters()) {
            System.out.println(" ПОБЕДИТЕЛЬ: КРОЛИК!");
        } else if (rabbit.getMeters() < turtle.getMeters()) {
            System.out.println(" ПОБЕДИТЕЛЬ: ЧЕРЕПАХА!");
        } else {
            System.out.println(" ПОБЕДИЛА ДРУЖБА!");
        }

    }
}