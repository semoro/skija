package org.jetbrains.skija;

import java.io.*;
import java.nio.file.*;
import org.jetbrains.skija.test.*;
import static org.jetbrains.skija.test.TestRunner.*;

public class ImageTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (Surface surface = Surface.makeRasterN32Premul(100, 100);
             Paint paint = new Paint().setColor(0xFFFF0000);
             Path path = new Path().moveTo(20, 80).lineTo(50, 20).lineTo(80, 80).closePath();)
        {
            Canvas canvas = surface.getCanvas();
            canvas.drawPath(path, paint);
            try (Image image = surface.makeImageSnapshot()) {
                new File("target/tests/ImageTest/").mkdirs();

                Files.write(java.nio.file.Paths.get("target/tests/ImageTest/polygon_default.png"), image.encodeToData().getBytes());
                Files.write(java.nio.file.Paths.get("target/tests/ImageTest/polygon_jpeg_default.jpeg"), image.encodeToData(EncodedImageFormat.JPEG).getBytes());
                Files.write(java.nio.file.Paths.get("target/tests/ImageTest/polygon_jpeg_50.jpeg"), image.encodeToData(EncodedImageFormat.JPEG, 50).getBytes());
                Files.write(java.nio.file.Paths.get("target/tests/ImageTest/polygon_webp_default.webp"), image.encodeToData(EncodedImageFormat.WEBP).getBytes());
                Files.write(java.nio.file.Paths.get("target/tests/ImageTest/polygon_webp_50.webp"), image.encodeToData(EncodedImageFormat.WEBP, 50).getBytes());
                // Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_heif_default.heif"), image.encodeToData(EncodedImageFormat.HEIF).getBytes());
                // Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_heif_50.heif"), image.encodeToData(EncodedImageFormat.HEIF, 50).getBytes());
            }
        }
    }
}