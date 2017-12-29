# Java DDS ImageIO Plugin
### Supports reading
  * **DXT1** / BC1
  * **DXT2** / BC2
  * **DXT5** / BC3
  * **ATI1** / BC4
  * **ATI2** / BC5
  * Most **Uncompressed** formats
  * _See all [supported formats](https://github.com/GoldenGnu/java-dds/wiki)._

### Does not support
  * Writing

## Info
The library is not actively being worked on, except for bug fixes and contributions.

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
<dependencies>
    <dependency>
        <groupId>net.nikr</groupId>
        <artifactId>dds</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
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
## Bugs
You can submit a bug report by creating a [new issue](https://github.com/GoldenGnu/java-dds/issues/new).

## Special Thanks
Java DDS ImageIO Plugin is based on code from the [GIMP DDS Plugin](http://code.google.com/p/gimp-dds/).

## Contribute
Want to join the project? Send an email to niklaskr@gmail.com
