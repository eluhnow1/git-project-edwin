import java.io.File;

public class Git {
    public static void main (String [] args) {
        
    }
    public static void initializeRepo (String repoName){
        File repo = new File (repoName);
        repo.mkdir();
        
    }
}