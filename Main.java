import controller.UserController;
import model.FileOperation;
import model.Repository;
import view.ViewUser;

public class Main {

    public static void main(String[] args) {
        FileOperation fileOperation = new FileOperation();
        Repository repository = new Repository(fileOperation);
        UserController controller = new UserController(repository);
        ViewUser view = new ViewUser(controller);
        view.run();
    }
}