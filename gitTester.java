import java.nio.file.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.*;
import java.io.FileReader;
import java.io.FileWriter;


public class gitTester {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        File example = new File ("./example.txt");
        System.out.println("******************************** Testing initialization: ******************************\n\n");
        Git seansGit = new Git("seansTestRepo");
        Git seansOtherGit = new Git ("seansTestRepo");
        System.out.println("^ Should say that the repo was initialized, and then already exists");
        Path indexPath = Paths.get("./seansTestRepo/git/index");
        Files.deleteIfExists(indexPath);
        System.out.println("...deleted index file, now trying to initialize the same repo...");
        Git seansNewGit = new Git ("seansTestRepo");
        System.out.println("^ Should say that the repo was initialized successfully.");
        seansNewGit.deleteRepo();
        seansGit.deleteRepo();
        seansOtherGit.deleteRepo();
        System.out.println("\n\n\n*************************** Testing blob creation ***************************");
        Git seansGit2 = new Git("seansTestRepo");
        seansGit2.setZipToggle(false);
        seansGit2.makeBlob(example);
        File index = new File ("./seansTestRepo/git/index");
        FileReader fr = new FileReader (index);
        System.out.print("\nIndex file contents after attempted blob creation:");
        while (fr.ready()){
            System.out.print((char)fr.read());
        }
        File hashFile = new File ("./seansTestRepo/git/objects/" + seansGit2.sha1Code(example.getPath()));
        if (hashFile.exists()){
            System.out.println("\n\nHashFile exists; blob was created successfully.");
        }
        fr.close();
        seansGit2 = resetTestFiles(seansGit2);
        System.out.println("\n\n\n******************************** Testing zip-compression for blob data ***************************\n\n");
        seansGit2.makeBlob(example);
        seansGit2.setZipToggle(true);
        seansGit2.makeBlob(example);
        FileReader freader = new FileReader (seansGit2.getIndex());
        System.out.print("\nIndex file contents after attempted blob creation:\n");
        while (freader.ready()){
            System.out.print((char)freader.read());
        }
        File compressedFile = new File ("./seansTestRepo/git/objects/" + seansGit2.sha1Code("./example.txt.zip"));
        System.out.println("\n The size of the uncompressed file is: " + hashFile.length() + " bytes. The length of the compressed file is: " + compressedFile.length() + " bytes.");
        freader.close();
        seansGit2.deleteRepo();
    }
    public static Git resetTestFiles (Git git) throws IOException{
        git.deleteRepo();
        return new Git("seansRepo");
    }

}
