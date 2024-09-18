import java.io.File;

public class Git {
    public static void main (String [] args) {
        
    }
    public static void initializeRepo (String repoName){
        File repo = new File (repoName);
        repo.mkdir();
        File git = new File ("git/objects");
        if (!git.exists()){
            git.mkdirs();
        }
        File 
    }
}