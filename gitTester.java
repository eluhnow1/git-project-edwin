import java.nio.file.*;
import java.io.IOException;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class gitTester {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // File example = new File ("./example.txt");
        // System.out.println("******************************** Testing initialization: ******************************\n\n");
        // Git seansGit = new Git("seansTestRepo");
        // Git seansOtherGit = new Git ("seansTestRepo");
        // System.out.println("^ Should say that the repo was initialized, and then already exists");
        // Path indexPath = Paths.get("./seansTestRepo/git/index");
        // Files.deleteIfExists(indexPath);
        // System.out.println("...deleted index file, now trying to initialize the same repo...");
        // Git seansNewGit = new Git ("seansTestRepo");
        // System.out.println("^ Should say that the repo was initialized successfully.");
        // seansNewGit.deleteRepo();
        // seansGit.deleteRepo();
        // seansOtherGit.deleteRepo();
        // System.out.println("\n\n\n*************************** Testing blob creation ***************************");
        // Git seansGit2 = new Git("seansTestRepo");
        // seansGit2.setZipToggle(false);
        // seansGit2.makeBlob(example);
        // File index = new File ("./seansTestRepo/git/index");
        // FileReader fr = new FileReader (index);
        // System.out.print("\nIndex file contents after attempted blob creation:");
        // while (fr.ready()){
        //     System.out.print((char)fr.read());
        // }
        // File hashFile = new File ("./seansTestRepo/git/objects/" + seansGit2.sha1Code(example.getPath()));
        // if (hashFile.exists()){
        //     System.out.println("\n\nHashFile exists; blob was created successfully.");
        // }
        // fr.close();
        // seansGit2 = resetTestFiles(seansGit2);
        // System.out.println("\n\n\n******************************** Testing zip-compression for blob data ***************************\n\n");
        // seansGit2.makeBlob(example);
        // seansGit2.setZipToggle(true);
        // seansGit2.makeBlob(example);
        // FileReader freader = new FileReader (seansGit2.getIndex());
        // System.out.print("\nIndex file contents after attempted blob creation:\n");
        // while (freader.ready()){
        //     System.out.print((char)freader.read());
        // }
        // File compressedFile = new File ("./seansTestRepo/git/objects/" + seansGit2.sha1Code("./example.txt.zip"));
        // System.out.println("\n The size of the uncompressed file is: " + hashFile.length() + " bytes. The length of the compressed file is: " + compressedFile.length() + " bytes.");
        // freader.close();
        //test readIndexFile
        







    
            //initialize git
            Git seansGit2 = new Git("seansTestRepo");

            // Test readIndexFile
            // String indexContents = seansGit2.readIndexFile();
            // System.out.println(indexContents);


            //Testing commit method with a file
            File example = new File ("./example.txt");
            seansGit2.makeBlob(example);
            seansGit2.commit("sean","yo yo yo");


            // test with folder now
            File testFolder = new File ("./testFolder");
            seansGit2.makeTree(testFolder);
            seansGit2.commit("collin","yo yo yo");

            // test stageCommits
            //with file
            //File example = new File ("./example.txt");
            seansGit2.stage("./example.txt");
            //now with folder
            File newFile = new File ("./testFolder");
            if (!newFile.exists()){
                System.out.println("File doesnt exists");
            }
            else{
                seansGit2.stage("./testFolder");

            }

            // test checkout
            //seansGit2.checkout("commitHash");


            seansGit2 = resetTestFiles(seansGit2);
            testFolderCompatibility(seansGit2);
            seansGit2.deleteRepo();
    }
    public static Git resetTestFiles (Git git) throws IOException{
        git.deleteRepo();
        return new Git("seansRepo");
    }
    private static void testFolderCompatibility(Git gitName) throws NoSuchAlgorithmException, IOException {
        System.out.println("\n\n\n******************************** Testing compatibility with non-empty directories ***************************\n\n");
        gitName.setZipToggle(false);
        File testPath = new File ("./testFolder");
        gitName.makeTree(testPath);
        File index = new File ("./seansRepo/git/index");
        FileReader fr = new FileReader (index);
        System.out.print("\nIndex file contents after attempted tree creation:");
        while (fr.ready()){
            System.out.print((char)fr.read());
        }
        fr.close();
        System.out.println("\nNumber of blobs in objects folder: " + (new File("./seansRepo/git/objects").list().length));
        System.out.println("Number of blobs expected in objects folder: 5");
    }

}
