import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.security.*;

public class Git {
    public static void main (String [] args) {
        
    }
    public static void initializeRepo (String repoName) throws IOException{
        File repo = new File (repoName);
        if (!repo.exists())
            repo.mkdir();
        File git = new File ("./" + repoName + "/git/objects");
        if (!git.exists() )
            git.mkdirs();
        Path indexPath = Paths.get("./" + repoName + "/git/index");
        Files.createFile (indexPath);
        File index = new File ("./" + repoName + "/git/index");
        if (repo.exists() && git.exists() && index.exists()){
            System.out.println("Git Repository already exists");
        }
    }
    public static void blob ( ){

    }
    public static String computeFileSHA1( File file ) throws IOException{
        String sha1 = null;
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance( "SHA-1" );
        }
        catch ( NoSuchAlgorithmException e1 )
        {
            throw new IOException( "Impossible to get SHA-1 digester", e1 );
        }
        try (InputStream input = new FileInputStream( file );
            DigestInputStream digestStream = new DigestInputStream( input, digest ) )
        {
            while(digestStream.read() != -1){
                // read file stream without buffer
            }
            MessageDigest msgDigest = digestStream.getMessageDigest();
            sha1 = new HexBinaryAdapter().marshal( msgDigest.digest() );
        }
        return sha1;
    }
}