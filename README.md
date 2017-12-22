# Java DDS ImageIO Plugin
## Supports reading
  * **DXT1** / BC1
  * **DXT2** / BC2
  * **DXT5** / BC3
  * **ATI1** / BC4
  * **ATI2** / BC5
  * Most **Uncompressed** formats.
_See all [supported formats](https://github.com/GoldenGnu/java-dds/wiki/Supported-Formats)._<br>
## Does not support
  * Writing

## Info
The library is not actively being worked on, except for bug fixes and contributions.<br>
<br>

## Help

To use in a maven project:

```xml
<repositories>
    <repository>
        <id>maven.nikr.net</id>
        <name>maven.nikr.net</name>
        <url>http://maven.nikr.net/</url>
    </repository>
</repositories>
...
<repositories>
    <repository>
        <id>maven.nikr.net</id>
        <name>maven.nikr.net</name>
        <url>http://maven.nikr.net/</url>
    </repository>
</repositories>
```
### Code Example

```java
File file = new File("image.dds");
BufferedImage image = null;
try {
    image = ImageIO.read(file);
} catch (IOException ex) {
    System.out.println("Failed to load: "+file.getName());
}
```
### Advanced Code Example:
```java
public BufferedImage read(File file, int imageIndex) throws IOException{
    Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix("dds");
    if (iterator.hasNext()){
        ImageReader imageReader = iterator.next();
        imageReader.setInput(new FileImageInputStream(file));
        int max = imageReader.getNumImages(true);
        if (imageIndex < max) return imageReader.read(imageIndex);
    }
    return null;
}
```
<br>
<h2>Bugs</h2>
You can submit a bug report by creating a <a href='https://github.com/GoldenGnu/java-dds/issues/new'>new issue</a>.<br>
<br>
<h2>Special Thanks</h2>
Java DDS ImageIO Plugin is based on code from the <a href='http://code.google.com/p/gimp-dds/'>GIMP DDS Plugin</a>.<br>
<br>
<h2>Contribute</h2>
Want to join the project?<br>
Send an email to niklaskr@gmail.com<br>
