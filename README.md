# AstroCraft BlockBattles (AstroBB)

## Important Note!

### You will have to use `gradle shadowJar` instead of `gradle build` to build the plugin!

## Developing

### Important
- Use correct spacing:  
  ```java
  public class TestClass() {
    public static void test(Stuff stuff) {
        if (stuff) {
            // Do stuff
        } else {
            // Do other stuff
        }
    }
  }
  ```
- Please don't check if the command was executed from the console or a player or if the executor has op, you can just use permissions in the `plugin.yml` file.
- If IntelliJ wants you to do a null check, then just do it.
- Adding comments in unreadable code won't kill you. (or just make readable code)
- The database doesn't require anything special, except the connect() and disconnect() in the startup logic and shutdown logic.
- Please use TODOs in your code, just do `// TODO: ...`

### Dependencies

If you add Dependencies, please add a comment what the dependency does, if it's not commonly used.  

### Committing

If you make any changes or add things, then please use the schema:  

**Title**: Short and should contain the biggest feature that you added.  
**Description**: A list with most of the features that you added and/or changed.
