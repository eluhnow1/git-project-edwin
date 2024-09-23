import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Git {
    public static void main (String [] args) throws IOException, NoSuchAlgorithmException {
        testInitialization();
        deleteRepo("seansTestRepo");
    }
    public static void testInitialization () throws IOException {
        initializeRepo("seansTestRepo");
        System.out.println("^ Should say that the repo already exists");
        Path indexPath = Paths.get("./seansTestRepo/git/index");
        Files.deleteIfExists(indexPath);
        System.out.println("...deleted index file, now trying to initialize the same repo...");
        initializeRepo("seansTestRepo");
        System.out.println("^ Should say that the repo was initialized successfully.");
    }
    public static void initializeRepo (String repoName) throws IOException{
        File repo = new File ("./" + repoName);
        File git = new File ("./" + repoName + "/git/objects");
        File index = new File ("./" + repoName + "/git/index");
        if (repo.exists() && git.exists() && index.exists()){
            System.out.println("Git Repository already exists");
            return;
        }
        if (!repo.exists())
            repo.mkdir();
        if (!git.exists())
            git.mkdirs();
        if (!index.exists()){
            Path indexPath = Paths.get("./" + repoName + "/git/index");
            Files.createFile (indexPath);
        }
        System.out.println("Repo '" + repoName + "' was initialized successfully");
    }
    public static void deleteRepo (String repoName) throws IOException{
        File repoToDelete = new File ("./" + repoName);
        deleteDir (repoToDelete);
        if (!repoToDelete.exists())
            System.out.println("Repo '" + repoName + "' was removed successfully");
    }

    private static void deleteDir (File file) throws IOException{
        if(file.exists() && file.isDirectory()){
            File [] files = file.listFiles();
            if (files.length > 0){
                for (int i = 0; i < files.length; i ++){
                    if (files[i].isDirectory()){
                        deleteDir(files[i]);
                        files[i].delete();
                    }
                    else{
                        files[i].delete();
                    }
                }
            }
            file.delete();
        }
        else{
            System.out.println("Could not delete directory");
        }
    }

    public static void blob ( ){

    }

    public static String sha1Code(String filePath) throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];
        // read all file content
        while (digestInputStream.read(bytes) > 0);

//      digest = digestInputStream.getMessageDigest();
        byte[] resultByteArry = digest.digest();
        return bytesToHexString(resultByteArry);
    }

    /**
     * Convert a array of byte to hex String. <br/>
     * Each byte is covert a two character of hex String. That is <br/>
     * if byte of int is less than 16, then the hex String will append <br/>
     * a character of '0'.
     *
     * @param bytes array of byte
     * @return hex String represent the array of byte
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;
            if (value < 16) {
                // if value less than 16, then it's hex String will be only
                // one character, so we need to append a character of '0'
                sb.append("0");
            }
            sb.append(Integer.toHexString(value).toUpperCase());
        }
        return sb.toString();
    }
}