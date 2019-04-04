import java.util.ArrayList;

class Project3 {
    public void run(ArrayList<String> args) {
        Engine engine = new Engine();

        if (args == null) {
            engine.startGame();
        } else {
            if (args.contains("--pvp")) {
                engine.startPVPGame(args.contains("--timed"));
            } else {
                engine.startGame();
            }
        }
    }

    public void testrun() {
        Tester tester = new Tester();
        tester.run();
    }

    public static void main(String[] args) {
        Project3 program = new Project3();

        if (args.length > 0) {
            ArrayList<String> argsList = new ArrayList<String>();
            for (String arg : args) {
                argsList.add(arg);
            }

            if (argsList.contains("--test")) {
                program.testrun();
            } else {
                program.run(argsList);
            }

        } else {
            program.run(null);
        }
    }
}