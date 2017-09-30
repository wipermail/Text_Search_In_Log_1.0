package sample.ext;

import java.io.File;

// Наследование от класса File для переопределения метода toString для возвращения только имени файла
public class ExtFile extends File {
    public ExtFile(String pathname) {
        super(pathname);
    }

    @Override
    public String toString() {
        return getName();
    }
}
