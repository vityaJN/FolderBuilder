
##Task
We have a folder structure represented by plain objects, eg:
class TreeItem{
 String name;
 List<TreeItem> children;
}
Create a function, that recieves two string lists, one for user readable folders, and one for
user writable folders, and returns a folder structure represented by a tree, that contains all
writable folders that can be reached via at least readable folder from the root. The tree
cannot contain folders that are not readable by the user (not even if it has writable
subfolders). Folders that are not writable must be included if they have writable subfolders,
but if it has no writable descendant, it has to be excluded, because it would just mislead the
user (trying to select a writable target folder for example).

public TreeItem getWritableFolderStructure(List<String> readableFolders,
List<String> writableFolders){
 // TODO implement
}
- Folders are always given with full paths (eg: /var/lib/jenkins)
- Paths that are in the writable list, are also in the readable list
- Folder names do not contain any special characters
- You can assume the folder structure fits into memory
