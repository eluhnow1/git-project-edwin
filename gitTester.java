import java.nio.file.*;
import java.io.IOException;
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
        File example = new File ("./example.txt");
        seansNewGit.zipFile(example);
    }
}
