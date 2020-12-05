import java.util.*;
import java.util.logging.Logger;

public class TreeItem {
    private String name;
    private Permission permission;
    private List<TreeItem> children;

    private final static Logger logger = Logger.getLogger(TreeItem.class.getName());

    private static boolean reachableFolderRules(TreeItem folder) {
        return folder.getPermission() == Permission.NONE || folder.children.size() == 0 && folder.getPermission() != Permission.WRITABLE;
    }

    public void removeNotReachableNodes() {
        children.removeIf(treeItem -> {
            treeItem.removeNotReachableNodes();
            return reachableFolderRules(treeItem);
        });
    }

    public TreeItem(String name) {
        this.name = name;
        this.permission = Permission.NONE;
        this.children = new ArrayList<>();
    }

    public static TreeItem createRootDirectory() {
        TreeItem rootDirectory = new TreeItem("");
        rootDirectory.setPermission(Permission.WRITABLE);
        return rootDirectory;
    }

    public TreeItem getFolder(String folder) {
        return children.stream()
                .filter(treeItem -> treeItem.getName().equals(folder))
                .findFirst()
                .orElse(null);
    }

    public void createFolder(List<String> readableList, Permission permission) {
        try {
            LinkedList<String> folderPath = new LinkedList<>(readableList);
            TreeItem folder = getFolder(folderPath.getFirst());

            if (folder == null) {
                folder = new TreeItem(folderPath.getFirst());
                addFolder(folder);
            }
            if (folderPath.getFirst().equals(folderPath.getLast())) {
                folder.setPermission(permission);
            }
            folderPath.remove(0);
            if (folderPath.size() > 0) {
                folder.createFolder(folderPath, permission);
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void setWritablePermissions(List<String> writableFolders, Permission permission) {
        try {
            LinkedList<String> folderPath = new LinkedList<>(writableFolders);
            TreeItem treeItem = findFolderInTheWholeTree(folderPath);
            if (treeItem != null) {
                treeItem.setPermission(permission);
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public TreeItem findFolderInTheWholeTree(List<String> folders) {
        try {
            LinkedList<String> folderPath = new LinkedList<>(folders);
            TreeItem folder = getFolder(folderPath.getFirst());
            if (folderPath.getFirst().equals(folderPath.getLast())) {
                return folder;
            }
            folderPath.remove(0);
            if (folderPath.size() > 0) {
                return folder.findFolderInTheWholeTree(folderPath);
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public void addFolder(TreeItem treeItem) {
        children.add(treeItem);
    }

    public List<TreeItem> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}


