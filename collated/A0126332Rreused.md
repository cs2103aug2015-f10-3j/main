# A0126332Rreused
###### src\test\cases\FullSystemFlowTest.java
``` java
    public void backUpData() {
        File file = new File(fileName);
        if (file.exists()) {
            backedUpContent = sController.getFileInBytes(fileName);
        }
    }
    
    public void restoreData() {
        if (backedUpContent != null) {
            sController.writeBytesToFile(fileName, backedUpContent, false);
        }
    }

}
```
