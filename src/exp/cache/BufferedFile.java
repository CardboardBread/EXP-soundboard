 package exp.cache;
 
 import java.io.BufferedInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.util.HashMap;
 
 public class BufferedFile extends TreadedByteBuffer
 {
   private final long fFileSize;
   private HashMap<String, Long> fReadingTracker;
   private File fFile;
   private BufferedInputStream fBufferedInput = null;
   private byte[] fByteBuffer;
   private long fLastCallMs;
   
   public BufferedFile(File file, int aBufferSize) {
     super(aBufferSize);
     this.fFileSize = file.length();
     this.fReadingTracker = new HashMap();
     try {
       this.fBufferedInput = new BufferedInputStream(new FileInputStream(this.fFile));
     } catch (FileNotFoundException e) {
       e.printStackTrace();
     }
     this.fByteBuffer = new byte[aBufferSize];
     this.fLastCallMs = System.currentTimeMillis();
   }
   
   public byte[] readNextBytes(String uuid) {
     this.fLastCallMs = System.currentTimeMillis();
     if (!this.fReadingTracker.containsKey(uuid)) {
       this.fReadingTracker.put(uuid, Long.valueOf(0L));
     }
     long totalRead = ((Long)this.fReadingTracker.get(uuid)).longValue();
     if (totalRead >= this.fFileSize) {
       try {
         this.fBufferedInput.close();
       } catch (IOException e) {
         e.printStackTrace();
       }
     }
     
     if (getCurrentBufferedSizeRounded() < this.fFileSize) {
       int bytesRead = 0;
       try {
         bytesRead = this.fBufferedInput.read(this.fByteBuffer);
       } catch (IOException e) {
         e.printStackTrace();
       }
       if (bytesRead > 0)
       {
 
         concat(this.fByteBuffer, bytesRead);
       }
       long prevRead = ((Long)this.fReadingTracker.get(uuid)).longValue();
       this.fReadingTracker.put(uuid, Long.valueOf(prevRead + bytesRead));
     }
     
     byte[] returnBytes = getNext(uuid);
     return returnBytes;
   }
   
   public long getFileSize() {
     return this.fFileSize;
   }
   
   public long getLastCallTime() {
     return this.fLastCallMs;
   }
   
   public File getFile() {
     return this.fFile;
   }
 }
 