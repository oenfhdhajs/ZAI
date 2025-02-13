package cn.z.zai.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@Component
public class IFileUtils {

    public String getImageString(String certPath) {

        String property = System.getProperty("user.dir");
        String localPath = property + certPath;
        File file = new File(localPath);
        if (!file.exists()) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {

                inputStream = this.getClass().getResourceAsStream(certPath);
                String substring = localPath.substring(0, localPath.lastIndexOf("/"));
                File directory = new File(substring);
                boolean mkdirs = directory.mkdirs();


                outputStream = Files.newOutputStream(Paths.get(localPath));


                byte[] buffer = new byte[1024];


                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }

        return localPath;
    }


}
