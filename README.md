# git-project-edwin
1) I coded stage() and it works with both files and directories ()
2) I coded commit() and it works (tested with multiple commits folders and files)
3) I attempted to do the extra credit checkout but I didn't really understand it
4) bugs that I fixed were: files and directories weren't being located correctly so it became hard to access, so I added checks to see if the file exists and also made sure that file paths were written correctly
Mismatches on the parameters for the interfaces and had to throw IOException and NOSuchAlgorithimException
I was stuck down a rabbit hole for the staging part because at first I thought I had to account for modified files. Then mistakenly, I thought that the purpose of staging was only to only to record new file additions in the index file without actually generating the corresponding tree or blob objects. So I hesitated to call the makeTree()/makeBlob() methods, thinking they shouldn't be used in th staging porcoss, But then I realized you can  call these methods because they not only generate the requiered tree/blob objects but update the index file making sure that new files and objects are staged. 
My head file wasn't being updated to latest commit, so future commits were not reference the parents. So I made a helper method called updateHead to help with this. 
debugged every SINGLE method for hours
5) example of a Git inferface  
GitInterface git = new Git ("myRepo");