import java.util.Arrays;
import java.util.List;

public class FolderBuilder {
    TreeItem rootDirectory = TreeItem.createRootDirectory();

    public TreeItem getWritableFolderStructure(List<String> readableFolders, List<String> writableFolders) {
        createFolders(readableFolders);
        setPermissions(writableFolders);
        rootDirectory.removeNotReachableNodes();
        return rootDirectory;
    }

    private void createFolders(List<String> readableFolders){
        for (String readableFolder : readableFolders) {
            String[] folders = readableFolder.split("/");
            rootDirectory.createFolder(Arrays.asList(folders).subList(1, folders.length), Permission.READABLE);
        }
    }

    private void setPermissions(List<String> writableFolders){
        for (String writableFolder : writableFolders) {
            String[] folders = writableFolder.split("/");
            rootDirectory.setWritablePermissions(Arrays.asList(folders).subList(1, folders.length), Permission.WRITABLE);
        }
    }

    //For testing purposes
    public TreeItem getRootDirectory() {
        return rootDirectory;
    }
}
