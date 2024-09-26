import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

public class Git {
    private String repoName;
    private File repo, git, objects, index;
    public boolean zipToggle = false;
    public Git (String repoName) throws IOException{
        this.repoName = repoName;
        repo = new File ("./" + repoName);
        git = new File ("./" + repoName + "/git");
        objects = new File ("./" + repoName + "/git/objects");
        index = new File ("./" + repoName + "/git/index");
        if (repo.exists() && git.exists() && objects.exists() && index.exists()){
            System.out.println("Git Repository already exists");
            return;
        }
        if (!repo.exists())
            repo.mkdir();
        if (!git.exists())
            git.mkdir();
        if (!objects.exists())
            objects.mkdir();
        if (!index.exists()){
            Path indexPath = Paths.get("./" + repoName + "/git/index");
            Files.createFile (indexPath);
        }
        System.out.println("Repo '" + repoName + "' was initialized successfully");
    }
    public File getIndex (){
        return index;
    }
    public void deleteRepo () throws IOException{
        File repoToDelete = new File ("./" + repoName);
        deleteDir (repoToDelete);
        repoToDelete.delete();
        if (!repoToDelete.exists())
            System.out.println("Repo '" + repoName + "' was removed successfully");
    }

    private static void deleteDir (File file) throws IOException{
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();            
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteDir(f);
                    }
                    else{
                        PrintWriter pw = new PrintWriter(f);
                        pw.write("");
                        pw.close();
                        if (!f.delete()){
                            throw new IOException("faled to delete file: " + f.getPath());
                        }
                    }
                }
            }            
            if (!file.delete()) {
                throw new IOException("Failed to delete directory: " + file.getPath());
            }
        } 
        else {
            System.out.println("Could not delete directory: either it does not exist or is not a directory.");
        }
    }
    
    public void makeBlob (File file) throws IOException, NoSuchAlgorithmException{
        //creating hash-file in objects folder and copying data from fileToCommit
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        //determining whether to compress or not
        File finalFile;
        if (zipToggle){
            finalFile = zipFile(file);
        }
        else
            finalFile = file;
        String hash = sha1Code(finalFile.getPath());
        File newCommit = new File ("./" + repoName + "/git/objects/" + hash);
        Path newCommitPath = Paths.get(newCommit.getPath());
        if(!newCommit.exists())
            Files.createFile(newCommitPath);
        else
            return;
        /*   implimentation for bytes and not just chars using fileStreams


        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(newCommit);
        int n;
        while ((n=fis.read()) != -1){
            fos.write(n);
        }
        fis.close();
        fos.close();


        */
        FileReader fr = new FileReader (finalFile);
        FileWriter fw = new FileWriter (newCommit);
        int c;
        while ((c = fr.read()) != -1){
            fw.write(c);
        }
        fr.close();
        fw.close();
        //updating index file in repo
        File indexFile = new File ("./" + repoName + "/git/index");
        updateIndexFile(indexFile, hash, finalFile);
    }
    public String makeTree(File directory) throws IOException, NoSuchAlgorithmException {
        if (!directory.exists() || !directory.isDirectory()) {
            throw new FileNotFoundException("Directory not found: " + directory.getPath());
        }
        //Gets the tree index contents
        StringBuilder treeContentsBuilder = new StringBuilder();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                treeContentsBuilder.append("tree " + makeTree(file) +  " " +file.getName() + "\n");
            }
            else {
                makeBlob(file);
                treeContentsBuilder.append("blob " + sha1Code(file.getPath()) +  " " +file.getName() + "\n");
            }
        }
        String treeContents = treeContentsBuilder.toString().trim();
        //Convert the contents to a temporary file
        File tempFile = new File("./" + repoName + "/git/objects/tempTree.txt");
        FileWriter fw = new FileWriter(tempFile);
        fw.write(treeContents);
        fw.close();
        //Optionally compress the file
        if (zipToggle) {
            tempFile = zipFile(tempFile);
        }
        //Calculate SHA1 hash of the file
        String treeHash = sha1Code(tempFile.getPath());
        
        //Adds the folder file to objects
        File newCommit = new File ("./" + repoName + "/git/objects/" + treeHash);
        if(!newCommit.exists())
            Files.createFile(Paths.get(newCommit.getPath()));
        //Writes content into git folder file
        FileReader fr = new FileReader (tempFile);
        fw = new FileWriter (newCommit);
        int c;
        while ((c = fr.read()) != -1){
            fw.write(c);
        }
        fr.close();
        fw.close();
        //deletes the temp file
        System.out.println("Deleting file: " + tempFile.getAbsolutePath());
        if (!tempFile.delete()) {
            System.err.println("Failed to delete temp file: " + tempFile.getPath());
        }
        
        //Adds the tree information to the index file
        updateIndexFile(index, treeHash, directory);
        return treeHash;
    }

    
    private void updateIndexFile (File indexFile, String hash, File finalFile) throws IOException{
        FileWriter fw = new FileWriter(indexFile, true);
        String fileType = "blob";
        if (finalFile.isDirectory()) {
            fileType = "tree";
        }
        fw.write(fileType + " " + hash + " " + finalFile.getPath() + "\n");
        fw.close();
    }
    public void setZipToggle (boolean b){
        zipToggle = b;
    }
    private File zipFile (File file) throws IOException, ZipException{
        if (!file.exists())
            throw new FileNotFoundException();
        File outputFile = new File(file.getName() + ".zip");
        final int BUFFER_SIZE = 4096;
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(outputFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zipEntry.setTime(0);
        zos.putNextEntry(zipEntry);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        // Read the file and write it to the ZipOutputStream
        while ((bytesRead = fis.read(buffer)) != -1) {
            zos.write(buffer, 0, bytesRead);
        }

        // Close the current ZipEntry
        zos.closeEntry();
        fis.close();
        fos.close();
        return outputFile;
    }
    public static String sha1Code(String filePath) throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];
        // read all file content
        while (digestInputStream.read(bytes) > 0);

        digest = digestInputStream.getMessageDigest();
        byte[] resultByteArry = digest.digest();
        digestInputStream.close();
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
    private static String bytesToHexString(byte[] bytes) {
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