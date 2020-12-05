import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FolderBuilderTest {

    private FolderBuilder folderBuilder = new FolderBuilder();

    @Test
    public void testBadInput(){
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();
        readableFolders.add("/users");
        readableFolders.add("/users//user");
        readableFolders.add("/users//user/hello");
        readableFolders.add("/users//user/hello/badinput");

        writableFolders.add("/users");
        writableFolders.add("/users//user");
        writableFolders.add("/users//user/hello");
        writableFolders.add("/users//user/hello/badinput");

        readableFolders.add("/users/////helloworld");
        writableFolders.add("/users/////helloworld");

        TreeItem rootDirectory = folderBuilder.getWritableFolderStructure(readableFolders,writableFolders);

        assertEquals("users",rootDirectory.findFolderInTheWholeTree(Collections.singletonList("users")).getName());
        assertNull(rootDirectory.findFolderInTheWholeTree(Collections.singletonList("/users//user/hello")));
        assertNull(rootDirectory.findFolderInTheWholeTree(Collections.singletonList("/users//user/hello/badinput")));
        assertNull(rootDirectory.findFolderInTheWholeTree(Collections.singletonList("/users/////helloworld")));
    }

    @Test
    public void getWritableFolderStructure() {
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();

        //User One
        readableFolders.add("/users");
        readableFolders.add("/users/user");
        readableFolders.add("/users/user/appdata");
        readableFolders.add("/users/user/appdata/roaming");
        readableFolders.add("/users/user/appdata/roaming/dontwritehere");

        writableFolders.add("/users");
        writableFolders.add("/users/user");
        writableFolders.add("/users/user/appdata");
        writableFolders.add("/users/user/appdata/roaming");


        //User Two
        readableFolders.add("/users/admin");
        readableFolders.add("/users/admin/password");
        readableFolders.add("/users/admin/password/RSA");

        TreeItem rootDirectory = folderBuilder.getWritableFolderStructure(readableFolders, writableFolders);

        assertEquals("", folderBuilder.rootDirectory.getName());
        assertEquals("users", rootDirectory.findFolderInTheWholeTree(Collections.singletonList("users")).getName());
        assertEquals("user", rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user")).getName());
        assertEquals("appdata", rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user", "appdata")).getName());
        assertEquals("roaming", rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user", "appdata", "roaming")).getName());
        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user", "appdata", "roaming", "dontwritehere")));


        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "admin")));
        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "admin", "password")));
        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "admin", "password", "RSA")));

    }

    @Test
    void TestWithOnlyOneWritableFolder() {
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();

        writableFolders.add("/users");

        TreeItem rootDirectory = folderBuilder.getWritableFolderStructure(readableFolders, writableFolders);
        assertEquals(0, rootDirectory.getChildren().size());
    }

    @Test
    void TestWithOnlyOneReadableFolder() {
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();

        readableFolders.add("/users");

        TreeItem rootDirectory = folderBuilder.getWritableFolderStructure(readableFolders, writableFolders);
        assertEquals(0, rootDirectory.getChildren().size());
    }

    @Test
    void writableSubfolderOfReadableFolder() {
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();

        readableFolders.add("/users");
        readableFolders.add("/users/user");

        readableFolders.add("/users/user/appdata");
        readableFolders.add("/users/user/appdata/local");
        readableFolders.add("/users/user/appdata/local/packages");

        writableFolders.add("/users/user/appdata");
        writableFolders.add("/users/user/appdata/local/packages");

        readableFolders.add("/users/admin");
        readableFolders.add("/users/admin/password");

        writableFolders.add("/users/admin/password");

        readableFolders.add("/users/shouldntContains");


        TreeItem rootDirectory = folderBuilder.getWritableFolderStructure(readableFolders, writableFolders);

        assertEquals("users", rootDirectory.findFolderInTheWholeTree(Collections.singletonList("users")).getName());
        assertEquals(Permission.READABLE, rootDirectory.findFolderInTheWholeTree(Collections.singletonList("users")).getPermission());

        assertEquals("appdata", rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user", "appdata")).getName());

        assertEquals(Permission.READABLE, rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user", "appdata", "local")).getPermission());

        assertEquals("packages", rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "user", "appdata", "local", "packages")).getName());

        assertEquals("password", rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "admin", "password")).getName());

        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("users", "shouldntContains")));
    }

    @Test
    void checkForPermissions() {
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();

        readableFolders.add("/user");
        readableFolders.add("/user/appdata");
        readableFolders.add("/user/appdata/local");

        writableFolders.add("/user/appdata/local");

        readableFolders.add("/user/documents/example");
        writableFolders.add("/user/documents/example");

        TreeItem rootDirectory = folderBuilder.getWritableFolderStructure(readableFolders, writableFolders);

        assertEquals(Permission.READABLE, rootDirectory.findFolderInTheWholeTree(Collections.singletonList("user")).getPermission());
        assertEquals(Permission.READABLE, rootDirectory.findFolderInTheWholeTree(Arrays.asList("user", "appdata")).getPermission());
        assertEquals(Permission.WRITABLE, rootDirectory.findFolderInTheWholeTree(Arrays.asList("user", "appdata", "local")).getPermission());

        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("user", "documents", "example")));
        assertNull(rootDirectory.findFolderInTheWholeTree(Arrays.asList("user", "documents")));
    }


    @Test
    void getFolder() {
        List<String> readableFolders = new LinkedList<>();
        List<String> writableFolders = new LinkedList<>();
        readableFolders.add("/users");
        readableFolders.add("/users/user");
        writableFolders.add("/users");
        writableFolders.add("/users/user");
        folderBuilder.getWritableFolderStructure(readableFolders, writableFolders);

        assertEquals("users", folderBuilder.getRootDirectory().getFolder("users").getName());
        assertEquals("user", folderBuilder.getRootDirectory().getFolder("users").getFolder("user").getName());
    }

    @Test
    void createRootDirectory() {
        TreeItem folder = folderBuilder.getRootDirectory();
        assertNotNull(folder);
        assertEquals("",folder.getName());
        assertEquals(Permission.WRITABLE,folder.getPermission());
        assertEquals(0, folder.getChildren().size());
    }
}