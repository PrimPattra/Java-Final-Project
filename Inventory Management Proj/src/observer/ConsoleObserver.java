package observer;
public class ConsoleObserver implements ProductObserver {
    @Override
    public void update(String message) {
        System.out.println("[Notification] " + message);
    }
}
