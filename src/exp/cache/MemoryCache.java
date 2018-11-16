 package exp.cache;
 
 import java.io.File;
 import java.lang.ref.SoftReference;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map.Entry;
 import java.util.concurrent.ConcurrentHashMap;
 
 public class MemoryCache
 {
   private long maxCacheBytes;
   private ConcurrentHashMap<String, SoftReference<BufferedFile>> fMap;
   
   public MemoryCache(int maxMegabytes)
   {
     this.fMap = new ConcurrentHashMap();
     this.maxCacheBytes = (maxMegabytes * 1000000);
   }
   
   public BufferedFile getBufferedFile(File file, int aBufferSize) {
     if (!this.fMap.containsKey(file.toString())) {
       BufferedFile bf = new BufferedFile(file, aBufferSize);
       SoftReference<BufferedFile> srbf = new SoftReference(bf);
       this.fMap.put(file.toString(), srbf);
       cacheMaintainance();
     }
     BufferedFile bf = (BufferedFile)((SoftReference)this.fMap.get(file.toString())).get();
     if (bf != null) {
       return bf;
     }
     this.fMap.remove(file.toString());
     return getBufferedFile(file, aBufferSize);
   }
   
 
 
 
   private void cacheMaintainance()
   {
     long currentSize = 0L;
     HashMap<String, Long> timeMap = new HashMap();
     for (SoftReference<BufferedFile> sf : this.fMap.values()) {
       BufferedFile bf = null;
       if ((bf = (BufferedFile)sf.get()) != null) {
         currentSize += bf.getCurrentBufferedSizeRounded();
         timeMap.put(bf.getFile().toString(), Long.valueOf(bf.getLastCallTime()));
       }
     }
     while (currentSize > this.maxCacheBytes) {
       removeOldestEntry(timeMap);
     }
   }
   
 
 
   private synchronized void removeOldestEntry(HashMap<String, Long> aTimeMap)
   {
     int oldest = 0;
     long oldestTime = 0L;
     ArrayList<Map.Entry<String, Long>> times = new ArrayList(aTimeMap.entrySet());
     for (int i = 0; i < times.size(); i++) {
       if (i == 0) {
         oldestTime = ((Long)((Map.Entry)times.get(0)).getValue()).longValue();
       }
       else if (((Long)((Map.Entry)times.get(i)).getValue()).longValue() < oldestTime) {
         oldestTime = ((Long)((Map.Entry)times.get(i)).getValue()).longValue();
         oldest = i;
       }
     }
     
     this.fMap.remove(((Map.Entry)times.get(oldest)).getKey());
   }
 }
