import java.nio.file.*;
import java.io.IOException;
public class gitTester {
    public static void main(String[] args) throws IOException {
        System.out.println("******************************** Testing initialization: ******************************\n\n");
        Git seansGit = new Git("seansTestRepo");
        Git seansOtherGit = new Git ("seansTestRepo");
        System.out.println("^ Should say that the repo was initialized, and then already exists");
        Path indexPath = Paths.get("./seansTestRepo/git/index");
        Files.deleteIfExists(indexPath);
        System.out.println("...deleted index file, now trying to initialize the same repo...");
        Git seansNewGit = new Git ("seansTestRepo");
        System.out.println("^ Should say that the repo was initialized successfully.");

        System.out.println();
    }
}
